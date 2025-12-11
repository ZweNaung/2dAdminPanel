package com.example.twodamin.data.repository

import com.example.twodamin.data.remote.dto.LuckyDeleteDto
import com.example.twodamin.data.remote.dto.LuckyDto
import com.example.twodamin.data.remote.response.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface LuckyRepository{
    suspend fun getAllLucky(): BaseResponse<List<LuckyDto>>

    suspend fun uploadLucky(
        name: RequestBody,
        section : RequestBody,
        image: MultipartBody.Part,
    ): BaseResponse<LuckyDto>

    suspend fun deleteLucky(
        luckyId: String
    ): BaseResponse<LuckyDeleteDto>
}