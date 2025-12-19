package com.example.twodamin.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ThreeDDto(
    @SerialName("_id")
    val id: String,

    @SerialName("result")
    val result: String,

    @SerialName("date")
    val date: String,

    @SerialName("__v")
    val v: Int? = null
)

@Serializable
data class ThreeDRequest(
    val result: String,
    val date: String
)