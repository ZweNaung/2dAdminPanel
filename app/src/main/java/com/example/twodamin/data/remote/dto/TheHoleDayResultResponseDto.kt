package com.example.twodamin.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
{
  "success": true,
  "data": {
    "_id": "685e60e6701353711f572bf8",
    "date": "27-06-2025",
    "child": [
      {
        "time": "11:00",
        "twoD": "99",
        "set": "999999",
        "value": "1111111",
        "_id": "685e60e6701353711f572bf9"
      },
      {
        "time": "12:00",
        "twoD": "12",
        "set": "111111",
        "value": "2222222",
        "_id": "685e60f6701353711f572bfd"
      },
      {
        "time": "3:00",
        "twoD": "30",
        "set": "333333",
        "value": "0000000",
        "_id": "685e6107701353711f572c02"
      },
      {
        "time": "4:00",
        "twoD": "43",
        "set": "444444",
        "value": "3333333",
        "_id": "685e6118701353711f572c08"
      }
    ],
    "__v": 0
  }
}
*/


@Serializable
data class TheHoleDayResultResponseDto(
    @SerialName("data")
    val `data`: Data?,
    @SerialName("success")
    val success: Boolean
) {
    @Serializable
    data class Data(
        @SerialName("child")
        val child: List<Child>,
        @SerialName("date")
        val date: String,
        @SerialName("_id")
        val id: String,
        @SerialName("__v")
        val v: Int
    ) {
        @Serializable
        data class Child(
            @SerialName("_id")
            val id: String,
            @SerialName("set")
            val `set`: String,
            @SerialName("time")
            val time: String,
            @SerialName("twoD")
            val twoD: String,
            @SerialName("value")
            val value: String,
        )
    }
}



// loadChild
@Serializable
data class UpdateChildResultResponse(
    val success: String,
    val updatedChild: UpdatedChild
)

@Serializable
data class UpdatedChild(
    @SerialName("_id")
    val id: String,
    val set: String,
    val time: String,
    val twoD: String,
    val value: String,
)


// updateChild
@Serializable
data class ChildUpdateRequestBody(
    @SerialName("set")
    val `set`: String,
    @SerialName("time")
    val time: String,
    @SerialName("twoD")
    val twoD: String,
    @SerialName("value")
    val value: String
)