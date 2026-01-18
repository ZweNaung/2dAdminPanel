package com.example.twodamin.presentation.screen.home

import com.example.twodamin.data.remote.dto.UpdateResultDto

data class UpdateResultState(
    val isLoading: Boolean = false,
    val updateResult: List<UpdateResultDto> =emptyList(),
    val error: String? = null,
    val isEntrySuccess : Boolean = false,
    val isDeleteSuccess : Boolean = false
)
