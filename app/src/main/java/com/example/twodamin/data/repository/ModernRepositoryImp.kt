package com.example.twodamin.data.repository

import com.example.twodamin.data.remote.api.ModernApiService
import com.example.twodamin.data.remote.dto.ModernDto
import com.example.twodamin.util.Resource

class ModernRepositoryImp(
    private val api: ModernApiService
) : ModernRepository {

    override suspend fun updateData(title: String, modern: String, internet: String): Resource<ModernDto> {
        return try {
            val response = api.updateModernData(ModernDto(title = title, modern = modern, internet = internet))
            if (response.success && response.data != null) {
                Resource.Success(response.data!!)
            } else {
                Resource.Error(message = response.message)
            }
        } catch (e: Exception) {
            Resource.Error(message = e.localizedMessage ?: "Unknown Error")
        }
    }

    override suspend fun getData(title: String): Resource<ModernDto> {
        return try {
            val response = api.getModernDataByTitle(title)
            if (response.success && response.data != null) {
                Resource.Success(response.data)
            } else {
                Resource.Error(message = response.message)
            }
        } catch (e: Exception) {
            Resource.Error(message = e.localizedMessage ?: "Unknown Error")
        }
    }
}