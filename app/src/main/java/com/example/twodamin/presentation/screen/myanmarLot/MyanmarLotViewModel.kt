package com.example.twodamin.presentation.screen.myanmarLot

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twodamin.data.repository.MyanmarLotRepository
import com.example.twodamin.util.Resource
import kotlinx.coroutines.launch
import java.io.File

class MyanmarLotViewModel(
    private val repository: MyanmarLotRepository
) : ViewModel() {

    // Compose UI အတွက် State
    private val _state = mutableStateOf(MyanmarLotState())
    val state: State<MyanmarLotState> = _state

    init {
        // ViewModel စစချင်းမှာ Data ဆွဲမယ်
        getAllMyanmarLots()
    }

    // 1. Get All Data
    fun getAllMyanmarLots() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            when (val result = repository.getAllMyanmarLots()) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        lotteryList = result.data ?: emptyList()
                    )
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                else -> Unit
            }
        }
    }

    // 2. Upload Data
    fun uploadMyanmarLot(file: File, name: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null, isUploadSuccess = false)

            when (val result = repository.uploadMyanmarLot(file, name)) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        isUploadSuccess = true
                    )
                    // Upload အောင်မြင်ရင် List ကို ပြန် refresh လုပ်မယ်
                    getAllMyanmarLots()
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                else -> Unit
            }
        }
    }

    // 3. Delete Data
    fun deleteMyanmarLot(id: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null, isDeleteSuccess = false)

            when (val result = repository.deleteMyanmarLot(id)) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        isDeleteSuccess = true
                    )
                    // Delete အောင်မြင်ရင် List ကို ပြန် refresh လုပ်မယ်
                    getAllMyanmarLots()
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                else -> Unit
            }
        }
    }

    // Snack bar ပြပြီးရင် Success state တွေကို ပြန် reset ချဖို့
    fun resetEvents() {
        _state.value = _state.value.copy(
            isUploadSuccess = false,
            isDeleteSuccess = false,
            error = null
        )
    }
}