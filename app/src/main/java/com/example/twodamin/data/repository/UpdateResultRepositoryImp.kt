package com.example.twodamin.data.repository

import com.example.twodamin.data.remote.api.UpdateResultApiService
import com.example.twodamin.data.remote.dto.UpdateResultDto
import com.example.twodamin.util.Resource

class UpdateResultRepositoryImpl(
    private val apiService: UpdateResultApiService
) : UpdateResultRepository {

    override suspend fun getUpdateResult(): Resource<List<UpdateResultDto>> {
        return try {
            val response = apiService.getUpdateResult()

            if (response.success && response.data != null) {
                Resource.Success(response.data)
            } else {
                Resource.Error(message = response.message)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(message = e.message ?: "An unknown error occurred")
        }
    }

    override suspend fun entryUpdateResult(body: UpdateResultDto): Resource<UpdateResultDto> {
        return try {
            val response = apiService.entryUpdateResult(body)

            if (response.success && response.data != null) {
                Resource.Success(response.data)
            } else {
                Resource.Error(message = response.message)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(message = e.message ?: "An unknown error occurred")
        }
    }
}