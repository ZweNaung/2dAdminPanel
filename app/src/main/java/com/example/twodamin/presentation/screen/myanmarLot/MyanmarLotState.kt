package com.example.twodamin.presentation.screen.myanmarLot

import com.example.twodamin.data.remote.dto.MMLotResponseDto

data class MyanmarLotState(
    val isLoading: Boolean = false,
    val lotteryList: List<MMLotResponseDto> = emptyList(), // List of Lottery
    val error: String? = null,
    val isUploadSuccess: Boolean = false, // For Upload
    val isDeleteSuccess: Boolean = false  // For Delete
)
