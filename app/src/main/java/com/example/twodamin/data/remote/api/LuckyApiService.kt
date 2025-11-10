package com.example.twodamin.data.remote.api

import com.example.twodamin.data.remote.dto.ViewAllLuckyDto
import retrofit2.http.GET

interface LuckyApiService {
    @GET("/lucky")
    suspend fun getAllLucky(): ViewAllLuckyDto

}