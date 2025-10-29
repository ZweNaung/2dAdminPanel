package com.example.twodamin.presentation.screen.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.twodamin.data.repository.GetByDateRepository

class EditViewModelFactory(private val repository: GetByDateRepository) : ViewModelProvider.Factory {
    override fun<T: ViewModel> create(modelClass: Class<T>):T{
        if (modelClass.isAssignableFrom(EditViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return EditViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknow ViewModel Class")
    }
}