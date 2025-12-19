package com.example.twodamin.data.remote.api

import com.example.twodamin.data.remote.dto.ThreeDDto
import com.example.twodamin.data.remote.dto.ThreeDRequest
import com.example.twodamin.data.remote.response.BaseResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ThreeDApiService {
    @GET("threeD/all")
    suspend fun getAllThreeD(): BaseResponse<List<ThreeDDto>>

    @POST("threeD")
    suspend fun threeDEntry(
        @Body request: ThreeDRequest
    ): BaseResponse<ThreeDDto>

    @DELETE("threeD/{id}")
    suspend fun deleteThreeD(
        @Path("id") threeDId: String
    ): BaseResponse<ThreeDDto>
}