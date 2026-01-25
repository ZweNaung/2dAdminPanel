package com.example.twodamin.data.remote.api

import com.example.twodamin.data.remote.dto.ThaiLotResponseDto
import com.example.twodamin.data.remote.response.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ThaiLotteryApiService {

    @GET("thaiLottery/all")
    suspend fun getAllThaiLottery(): BaseResponse<List<ThaiLotResponseDto>>

    @Multipart
    @POST("thaiLottery/")
    suspend fun uploadThaiLottery(
        @Part image: MultipartBody.Part, // name="thaiLotImg" ဖြစ်ရမယ်
        @Part("name") name: RequestBody
    ): BaseResponse<ThaiLotResponseDto>

    // Delete မှာ Serialization error မတက်အောင် Return type ကို DTO ထားလိုက်ပါ
    @DELETE("thaiLottery/{id}")
    suspend fun deleteThaiLottery(
        @Path("id") id: String
    ): BaseResponse<ThaiLotResponseDto>
}