package com.example.twodamin.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ModernDto(
    @SerialName("_id") val id: String? = null,
    @SerialName("title") val title: String,
    @SerialName("modern") val modern: String,
    @SerialName("internet") val internet: String
)