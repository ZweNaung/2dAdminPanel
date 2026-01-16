package com.example.twodamin.data.remote.api

import com.example.twodamin.data.remote.dto.ModernDto
import com.example.twodamin.data.remote.response.BaseResponse
import retrofit2.http.*

interface ModernApiService {
    @POST("modern/")
    suspend fun updateModernData(
        @Body data: ModernDto
    ): BaseResponse<ModernDto>

    @GET("modern/{title}")
    suspend fun getModernDataByTitle(
        @Path("title") title: String
    ): BaseResponse<ModernDto>
}