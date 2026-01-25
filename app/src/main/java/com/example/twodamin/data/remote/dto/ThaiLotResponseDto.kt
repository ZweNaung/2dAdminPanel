package com.example.twodamin.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


    @Serializable
    data class ThaiLotResponseDto(
        @SerialName("_id")
        val id: String,
        @SerialName("imgUrl")
        val imgUrl: String,
        @SerialName("name")
        val name: String,
        @SerialName("__v")
        val v: Int
    )
