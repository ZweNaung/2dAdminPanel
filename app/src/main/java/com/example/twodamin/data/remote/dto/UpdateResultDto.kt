package com.example.twodamin.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateResultDto(
    @SerialName("_id")
    val id: String? = null, // GET မှာပါလာမယ်၊ POST (Input) မှာမလိုဘူး

    @SerialName("twoD")
    val twoD: String,

    @SerialName("set")
    val set: String,

    @SerialName("value")
    val value: String,

    @SerialName("session")
    val session: String // "12:01 PM" or "4:30 PM"
)
