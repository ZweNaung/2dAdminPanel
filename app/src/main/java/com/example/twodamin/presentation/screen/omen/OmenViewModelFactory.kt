package com.example.twodamin.presentation.screen.omen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.twodamin.data.repository.OmenRepository
import kotlin.jvm.Throws

class OmenViewModelFactory(private val repository: OmenRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(OmenViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return OmenViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknow ViewModel Class")
    }
}