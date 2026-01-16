package com.example.twodamin.presentation.screen.lucky

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.twodamin.data.repository.LuckyRepository

class LuckyViewModelFactory(private val repository: LuckyRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LuckyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LuckyViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknow ViewModel Class")
    }
}