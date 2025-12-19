package com.example.twodamin.presentation.screen.threeD

import androidx.compose.ui.util.fastCbrt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
import com.example.twodamin.data.remote.dto.ThreeDDto
import com.example.twodamin.data.repository.ThreeDRepository
import com.example.twodamin.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ThreeDViewModel(
    private val repository: ThreeDRepository
): ViewModel() {

    private val _state = MutableStateFlow(ThreeDState())
    val state = _state.asStateFlow()

    init {
        getAllThreeD()
    }
    fun getAllThreeD(){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            when(val result = repository.getAllThreeD()){
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            threeDList = result.data ?: emptyList(),
                            error = null
                        )
                    }
                }
                is Resource.Error ->{
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.message ?: "Unknow Error"
                        )
                    }
                }
                else -> Unit
            }
        }
    }

    fun entryThreeD(result: String,date:String){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            when(val response = repository.threeDEntry(result,date)){
                is Resource.Success ->{
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isEntrySuccess = true,
                            error = null
                        )
                    }
                    getAllThreeD()
                }
                is Resource.Error ->{
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = response.message
                        )
                    }
                }
                else -> Unit
            }
        }
    }

    fun deleteThreeD(id: String){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, isDeleteSuccess = false) }

            when(val response = repository.deleteThreeD(id)){
                is Resource.Success ->{
                    _state.update { it.copy(isLoading = false, isDeleteSuccess = true) }
                getAllThreeD()
                }
                is Resource.Error ->{
                    _state.update { it.copy(isLoading = false, error = response.message) }
                }
                else -> Unit
            }
        }
    }
    fun resetEntrySuccess(){
        _state.update { it.copy(isEntrySuccess = false, isDeleteSuccess = false, error = null) }
    }
}