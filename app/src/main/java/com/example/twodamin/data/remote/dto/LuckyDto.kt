package com.example.twodamin.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//getAll-create
@Serializable
data class LuckyDto(
    @SerialName("_id")
    val id: String,

    @SerialName("name")
    val name: String,

    @SerialName("imgUrl")
    val imgUrl: String,

    @SerialName("section")
    val section: String // "week" or "day"
)


//delete
@Serializable
data class LuckyDeleteDto(
    @SerialName("_id")
    val id: String,

    @SerialName("name")
    val name: String
)