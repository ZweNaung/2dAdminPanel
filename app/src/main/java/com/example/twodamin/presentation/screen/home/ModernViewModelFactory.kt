package com.example.twodamin.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.twodamin.data.repository.ModernRepository

class ModernViewModelFactory(private val repository: ModernRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ModernViewModel::class.java)) {
            return ModernViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}