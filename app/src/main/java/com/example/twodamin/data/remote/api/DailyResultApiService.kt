package com.example.twodamin.data.remote.api

import com.example.twodamin.data.remote.dto.ChildUpdateRequestBody
import com.example.twodamin.data.remote.dto.TheHoleDayResultResponseDto
import com.example.twodamin.data.remote.dto.UpdateChildResultResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface DailyResultApiService {
    @GET("dailyResult/{date}")
    suspend fun getByDate(@Path("date") date: String): TheHoleDayResultResponseDto

    @PUT("updateChild/{date}/child/{childId}")
    suspend fun updateChildResult(
        @Path("date") date: String,
        @Path("childId") childId: String,
        @Body requestBody: ChildUpdateRequestBody
    ): UpdateChildResultResponse
}