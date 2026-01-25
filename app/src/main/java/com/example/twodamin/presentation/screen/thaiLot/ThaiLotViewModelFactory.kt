package com.example.twodamin.presentation.screen.thaiLot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.twodamin.data.repository.ThaiLotteryRepository

class ThaiLotViewModelFactory(
    private val repository: ThaiLotteryRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ThaiLotViewModel::class.java)) {
            return ThaiLotViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}