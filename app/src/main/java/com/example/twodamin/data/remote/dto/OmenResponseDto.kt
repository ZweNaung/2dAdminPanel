package com.example.twodamin.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
{
  "success": true,
  "message": "Fetch Data Successfully.",
  "data": [
    {
      "_id": "68f0efc0223be4b207f381c3",
      "name": "android",
      "imgUrl": "https://cdn.mmsub.asia/omen/1760620478734-channels4_profile.jpg",
      "__v": 0
    },
    {
      "_id": "68f0efd4223be4b207f381c5",
      "name": "arrow",
      "imgUrl": "https://cdn.mmsub.asia/omen/1760620499764-410-4101667_red-curved-arrow-png-curved-red-arrow-png.png",
      "__v": 0
    },
    {
      "_id": "68f1dd84793426c09b97fc0b",
      "name": "batman",
      "imgUrl": "https://cdn.mmsub.asia/omen/1760681347000-batman_resize.png",
      "__v": 0
    }
  ]
}
*/


//View All Data
@Serializable
data class OmenViewAllResponseDto(
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

//Data Insert
@Serializable
data class OmenUploadResponseDto(
    @SerialName("success")
    val success: Boolean,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val `data`:Data
){
@Serializable
data class Data(
    @SerialName("_id")
    val id: String,
    @SerialName("name")
    val name: String
)}

//Delete
@Serializable
data class OmenDeleteResponseDto(
    @SerialName("success")
    val success: Boolean,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val `data`: Data
) {
    @Serializable
    data class Data(
        @SerialName("_id")
        val id: String,
        @SerialName("name")
        val name: String
    )
}


//update
@Serializable
data class OmenUpdateResponseDto(
    @SerialName("success")
    val success: Boolean,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val `data`: Data
) {
    @Serializable
    data class Data(
        @SerialName("_id")
        val id: String,
        @SerialName("name")
        val name: String,
        @SerialName("imgUrl")
        val imgUrl: String,
        @SerialName("__v")
        val v: Int
    )
}
