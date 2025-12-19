package com.example.twodamin.data.repository

import com.example.twodamin.data.remote.dto.ThreeDDto
import com.example.twodamin.data.remote.dto.ThreeDRequest
import com.example.twodamin.data.remote.response.BaseResponse
import com.example.twodamin.util.Resource

interface ThreeDRepository {
    suspend fun getAllThreeD(): Resource<List<ThreeDDto>>

    suspend fun threeDEntry(
        result: String,
        date: String
    ): Resource<ThreeDDto>

    suspend fun deleteThreeD(
        threeDId: String
    ): Resource<ThreeDDto>
}