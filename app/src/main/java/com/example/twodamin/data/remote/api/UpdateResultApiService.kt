package com.example.twodamin.data.remote.api

import com.example.twodamin.data.remote.dto.UpdateResultDto
import com.example.twodamin.data.remote.response.BaseResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UpdateResultApiService {
    @GET("live-result")
    suspend fun getUpdateResult(): BaseResponse<List<UpdateResultDto>>

    @POST("live-result")
    suspend fun entryUpdateResult(
        @Body requestBody: UpdateResultDto
    ): BaseResponse<UpdateResultDto>
}