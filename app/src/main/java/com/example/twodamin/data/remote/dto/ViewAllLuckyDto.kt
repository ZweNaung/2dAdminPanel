package com.example.twodamin.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
{
    "success": true,
    "message": "Fetch Data Successfully",
    "data": [
        {
            "_id": "691014b92af0cc84d9d372f1",
            "name": "batman",
            "imgUrl": "https://cdn.mmsub.asia/lucky/1762661560361-batman_resize.png",
            "__v": 0
        }
    ]
}
*/
@Serializable
data class ViewAllLuckyDto(
    @SerialName("data")
    val `data`: List<Data>,
    @SerialName("message")
    val message: String,
    @SerialName("success")
    val success: Boolean
) {
    @Serializable
    data class Data(
        @SerialName("_id")
        val id: String,
        @SerialName("imgUrl")
        val imgUrl: String,
        @SerialName("name")
        val name: String,
        @SerialName("__v")
        val v: Int
    )
}