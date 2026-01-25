package com.example.twodamin.data.repository

import com.example.twodamin.data.remote.api.ThaiLotteryApiService
import com.example.twodamin.data.remote.dto.ThaiLotResponseDto
import com.example.twodamin.util.Resource
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ThaiLotteryRepositoryImpl(
    private val apiService: ThaiLotteryApiService
) : ThaiLotteryRepository {

    override suspend fun getAllThaiLots(): Resource<List<ThaiLotResponseDto>> {
        return try {
            val response = apiService.getAllThaiLottery()
            if (response.success && response.data != null) {
                Resource.Success(response.data)
            } else {
                Resource.Error(message = response.message)
            }
        } catch (e: Exception) {
            Resource.Error(message = e.localizedMessage ?: "Unknown Error")
        }
    }

    override suspend fun uploadThaiLot(file: File, name: String): Resource<ThaiLotResponseDto> {
        return try {
            val nameRequestBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())

            // 🔥 Backend Router: upload.single('thaiLotImg')
            val imagePart = MultipartBody.Part.createFormData("thaiLotImg", file.name, requestFile)

            val response = apiService.uploadThaiLottery(imagePart, nameRequestBody)

            if (response.success && response.data != null) {
                Resource.Success(response.data)
            } else {
                Resource.Error(message = response.message)
            }
        } catch (e: Exception) {
            Resource.Error(message = e.localizedMessage ?: "Unknown Error")
        }
    }

    override suspend fun deleteThaiLot(id: String): Resource<String> {
        return try {
            val response = apiService.deleteThaiLottery(id)
            if (response.success) {
                Resource.Success(response.message)
            } else {
                Resource.Error(message = response.message)
            }
        } catch (e: Exception) {
            Resource.Error(message = e.localizedMessage ?: "Unknown Error")
        }
    }
}