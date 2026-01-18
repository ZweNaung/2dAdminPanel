package com.example.twodamin.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twodamin.data.remote.dto.UpdateResultDto
import com.example.twodamin.data.repository.UpdateResultRepository
import com.example.twodamin.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UpdateResultViewModel(private val repository: UpdateResultRepository) : ViewModel(){
    private val _state = MutableStateFlow(UpdateResultState())
    val state = _state.asStateFlow()

    init {
        getResults()
    }

    fun getResults() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            when (val result = repository.getUpdateResult()) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            updateResult = result.data ?: emptyList(),
                            error = null
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
                else -> Unit
            }
        }
    }

    // 2. POST Method - Entry Data
    fun entryResult(twoD: String, set: String, value: String, session: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, isEntrySuccess = false) }

            val requestBody = UpdateResultDto(
                twoD = twoD,
                set = set,
                value = value,
                session = session
            )

            when (val result = repository.entryUpdateResult(requestBody)) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isEntrySuccess = true,
                            error = null
                        )
                    }
                    getResults()
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isEntrySuccess = false,
                            error = result.message
                        )
                    }
                }
                else -> Unit
            }
        }
    }

    fun resetEntryState() {
        _state.update { it.copy(isEntrySuccess = false, error = null) }
    }
}