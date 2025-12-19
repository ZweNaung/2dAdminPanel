package com.example.twodamin.presentation.screen.threeD

import com.example.twodamin.data.remote.dto.ThreeDDto

data class ThreeDState(
    val isLoading: Boolean = false,
    val threeDList: List<ThreeDDto> =emptyList(),
    val error: String? = null,
    val isEntrySuccess : Boolean = false,
    val isDeleteSuccess : Boolean = false
)
