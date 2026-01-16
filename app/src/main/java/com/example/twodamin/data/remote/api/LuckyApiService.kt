package com.example.twodamin.data.remote.api

import com.example.twodamin.data.remote.dto.LuckyDeleteDto
import com.example.twodamin.data.remote.dto.LuckyDto
import com.example.twodamin.data.remote.response.BaseResponse
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface LuckyApiService {
    @GET("lucky")
    suspend fun getAllLucky(): BaseResponse<List<LuckyDto>>

    @Multipart
    @POST("lucky")
    suspend fun uploadLucky(
        @Part("name")  name: RequestBody,
         @Part("section") section: RequestBody,
        @Part  image:  MultipartBody.Part
    ):  BaseResponse<LuckyDto>

    @DELETE("lucky/{id}")
    suspend fun deleteLucky(
        @Path("id") luckyId: String
    ):  BaseResponse<LuckyDeleteDto>
}