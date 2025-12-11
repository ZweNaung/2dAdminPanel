package com.example.twodamin.data.repository

import com.example.twodamin.data.remote.dto.OmenDeleteResponseDto
import com.example.twodamin.data.remote.dto.OmenUpdateResponseDto
import com.example.twodamin.data.remote.dto.OmenUploadResponseDto
import com.example.twodamin.data.remote.dto.OmenViewAllResponseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

interface OmenRepository {
    suspend fun getAllOmen(): OmenViewAllResponseDto

    suspend fun uploadOmen(
        name: RequestBody,
        image: MultipartBody.Part,
    ): OmenUploadResponseDto

    suspend fun deleteOmen(
        omenId: String
    ): OmenDeleteResponseDto

    suspend fun updateOmen(
        omenId: String,
        name: RequestBody?,
        image: MultipartBody.Part?
    ): OmenUpdateResponseDto
}


