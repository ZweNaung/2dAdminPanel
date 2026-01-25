package com.example.twodamin.presentation.screen.myanmarLot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.twodamin.data.repository.MyanmarLotRepository

class MyanmarLotViewModelFactory(
    private val repository: MyanmarLotRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyanmarLotViewModel::class.java)) {
            return MyanmarLotViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}