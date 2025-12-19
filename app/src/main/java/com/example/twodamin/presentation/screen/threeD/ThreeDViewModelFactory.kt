package com.example.twodamin.presentation.screen.threeD

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.twodamin.data.repository.ThreeDRepository

class ThreeDViewModelFactory(private val repository: ThreeDRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ThreeDViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return ThreeDViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknow ViewModel Class")
    }
}