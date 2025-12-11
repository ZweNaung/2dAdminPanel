package com.example.twodamin.data.remote.api

import com.example.twodamin.data.remote.dto.ApiResponse
import com.example.twodamin.data.remote.dto.DataEntryDto
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface DataEntryApiService {
        @POST("insert/{time}")
        suspend fun getInsert(@Path("time") timePath: String,@Body data: DataEntryDto): ApiResponse
}


//    @POST("insert/12pm")
//    suspend fun getInsert(@Body data: ElevenModelRequest): ApiResponse
