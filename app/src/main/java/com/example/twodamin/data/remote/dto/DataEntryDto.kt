package com.example.twodamin.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DataEntryDto(
    val date: String,
    val time: String,
    val twoD: String,
    val set: String,
    val value: String
)

@Serializable
data class ApiResponse(
    val message: String
)