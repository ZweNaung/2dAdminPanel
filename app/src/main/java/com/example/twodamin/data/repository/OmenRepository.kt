package com.example.twodamin.data.repository

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
}


