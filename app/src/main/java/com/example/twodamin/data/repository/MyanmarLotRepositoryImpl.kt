package com.example.twodamin.data.repository

import com.example.twodamin.data.remote.api.MyanmarLotApiService
import com.example.twodamin.data.remote.dto.MMLotResponseDto
import com.example.twodamin.util.Resource
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException

class MyanmarLotRepositoryImpl(
    private val apiService: MyanmarLotApiService
) : MyanmarLotRepository {

    override suspend fun getAllMyanmarLots(): Resource<List<MMLotResponseDto>> {
        return try {
            val response = apiService.getAllMyanmarLots()
            if (response.success && response.data != null) {
                Resource.Success(response.data)
            } else {
                Resource.Error(message = response.message)
            }
        } catch (e: Exception) {
            handleException(e)
        }
    }

    override suspend fun uploadMyanmarLot(file: File, name: String): Resource<MMLotResponseDto> {
        return try {
            // 1. Prepare Name RequestBody
            val nameRequestBody = name.toRequestBody("text/plain".toMediaTypeOrNull())

            // 2. Prepare Image MultipartBody
            // backend router: upload.single('myanmarLotImg')
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("myanmarLotImg", file.name, requestFile)

            // 3. Call API
            val response = apiService.uploadMyanmarLot(imagePart, nameRequestBody)

            if (response.success && response.data != null) {
                Resource.Success(response.data)
            } else {
                Resource.Error(message = response.message)
            }
        } catch (e: Exception) {
            handleException(e)
        }
    }

    override suspend fun deleteMyanmarLot(id: String): Resource<String> {
        return try {
            // API ကို လှမ်းခေါ်မယ်
            val response = apiService.deleteMyanmarLot(id)

            if (response.success) {
                // Success ဖြစ်ရင် message ပြန်မယ်
                Resource.Success(response.message)
            } else {
                Resource.Error(message = response.message)
            }
        } catch (e: Exception) {
            e.printStackTrace() // Logcat မှာ error ကြည့်လို့ရအောင်
            Resource.Error(message = e.localizedMessage ?: "Unknown Error")
        }
    }

    // Error Handling Helper Function
    private fun <T> handleException(e: Exception): Resource<T> {
        return when (e) {
            is IOException -> Resource.Error(message = "Network Error: Please check your internet connection.")
            else -> Resource.Error(message = e.localizedMessage ?: "Unknown Error occurred")
        }
    }
}