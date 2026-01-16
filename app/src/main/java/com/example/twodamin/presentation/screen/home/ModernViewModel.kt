package com.example.twodamin.presentation.screen.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twodamin.data.repository.ModernRepository
import com.example.twodamin.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ModernViewModel(private val repository: ModernRepository): ViewModel() {
    private val _state= MutableStateFlow(ModernState())
    val state: StateFlow<ModernState> =_state.asStateFlow()

    fun updateModern(title: String,modern: String,internet: String){
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true
            )
            when(val result =repository.updateData(title,modern,internet)){
                is Resource.Success ->{
                    _state.value = _state.value.copy(isLoading = false, isEntrySuccess = true,error = null)
                }
                is Resource.Error ->{
                    _state.value = _state.value.copy(isLoading = false, isEntrySuccess = false ,error = result.message)
                }
                else -> {_state.value = _state.value.copy(isLoading = false)}
            }
        }
    }
}