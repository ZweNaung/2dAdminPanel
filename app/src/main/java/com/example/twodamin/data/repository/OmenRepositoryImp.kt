package com.example.twodamin.data.repository

import com.example.twodamin.data.remote.api.OmenApiService
import com.example.twodamin.data.remote.dto.ChildUpdateRequestBody
import com.example.twodamin.data.remote.dto.OmenDeleteResponseDto
import com.example.twodamin.data.remote.dto.OmenUpdateResponseDto
import com.example.twodamin.data.remote.dto.OmenUploadResponseDto
import com.example.twodamin.data.remote.dto.OmenViewAllResponseDto
import com.example.twodamin.data.remote.dto.UpdateChildResultResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class OmenRepositoryImp(private val omenApiService: OmenApiService): OmenRepository {
    override suspend fun getAllOmen(): OmenViewAllResponseDto {
        return omenApiService.getAllOmen()
    }

    override suspend fun uploadOmen(
        name: RequestBody,
        image: MultipartBody.Part,
    ): OmenUploadResponseDto {
        return omenApiService.uploadOmen(
            name = name,
            image = image
        )
    }

    override suspend fun deleteOmen(omenId: String): OmenDeleteResponseDto {
        return omenApiService.deleteOmen(
            omenId = omenId
        )
    }

    override suspend fun updateOmen(
        omenId: String,
        name: RequestBody?,
        image: MultipartBody.Part?): OmenUpdateResponseDto {
        return omenApiService.updateOmen(
            omenId = omenId,
            name = name,
            image = image
        )
    }
}

