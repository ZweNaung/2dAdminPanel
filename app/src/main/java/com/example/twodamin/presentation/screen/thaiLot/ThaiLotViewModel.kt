package com.example.twodamin.presentation.screen.thaiLot

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twodamin.data.repository.ThaiLotteryRepository
import com.example.twodamin.util.Resource
import kotlinx.coroutines.launch
import java.io.File

class ThaiLotViewModel(
    private val repository: ThaiLotteryRepository
) : ViewModel() {

    private val _state = mutableStateOf(ThaiLotState())
    val state: State<ThaiLotState> = _state

    init {
        getAllThaiLots()
    }

    fun getAllThaiLots() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            when (val result = repository.getAllThaiLots()) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(isLoading = false, lotteryList = result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(isLoading = false, error = result.message)
                }
                else -> Unit
            }
        }
    }

    fun uploadThaiLot(file: File, name: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, isUploadSuccess = false, error = null)
            when (val result = repository.uploadThaiLot(file, name)) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(isLoading = false, isUploadSuccess = true)
                    getAllThaiLots() // Refresh List
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(isLoading = false, error = result.message)
                }
                else -> Unit
            }
        }
    }

    fun deleteThaiLot(id: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, isDeleteSuccess = false, error = null)
            when (val result = repository.deleteThaiLot(id)) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(isLoading = false, isDeleteSuccess = true)
                    getAllThaiLots() // Refresh List
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(isLoading = false, error = result.message)
                }
                else -> Unit
            }
        }
    }

    fun resetEvents() {
        _state.value = _state.value.copy(
            isUploadSuccess = false,
            isDeleteSuccess = false,
            error = null
        )
    }
}