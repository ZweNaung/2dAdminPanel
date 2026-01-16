package com.example.twodamin.presentation.screen.home

import com.example.twodamin.data.remote.dto.ModernDto
import com.example.twodamin.data.remote.dto.ThreeDDto

data class ModernState(
    val isLoading: Boolean = false,
    val modernList: List<ModernDto> =emptyList(),
    val error: String? = null,
    val isEntrySuccess : Boolean = false,
    val isDeleteSuccess : Boolean = false
)
