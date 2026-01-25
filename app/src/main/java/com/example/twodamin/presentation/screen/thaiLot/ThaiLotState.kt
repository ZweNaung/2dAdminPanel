package com.example.twodamin.presentation.screen.thaiLot

import com.example.twodamin.data.remote.dto.ThaiLotResponseDto

data class ThaiLotState(
    val isLoading: Boolean = false,
    val lotteryList: List<ThaiLotResponseDto> = emptyList(),
    val error: String? = null,
    val isUploadSuccess: Boolean = false,
    val isDeleteSuccess: Boolean = false
)
