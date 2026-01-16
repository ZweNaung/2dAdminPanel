package com.example.twodamin.data.repository

import android.util.Log
import com.example.twodamin.data.remote.api.ThreeDApiService
import com.example.twodamin.data.remote.dto.ThreeDDto
import com.example.twodamin.data.remote.dto.ThreeDRequest
import com.example.twodamin.data.remote.response.BaseResponse
import com.example.twodamin.util.Resource
import retrofit2.HttpException
import java.io.IOException

class ThreeDRepositoryImpl (private val api: ThreeDApiService): ThreeDRepository{

    override suspend fun getAllThreeD(): Resource<List<ThreeDDto>> {
        return try {
            val response = api.getAllThreeD()
            if(response.success && response.data !=null){
                Resource.Success(response.data)
            }else{
                Resource.Error(message = response.message)
            }
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Error(message = e.message ?: "An unkow error occurred")
        }
    }

    override suspend fun threeDEntry(
        result: String,
        date: String
    ): Resource<ThreeDDto> {
        return try {

            Log.d("API_TEST", "Sending: $result, $date")

            val request = ThreeDRequest(
                result = result,
                date = date
            )
            val response = api.threeDEntry(request)

            Log.d("API_TEST", "Response: ${response.message}")

            if (response.success && response.data != null) {
                Resource.Success(response.data!!)
            } else {
                Resource.Error(message = response.message)
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try {
                errorBody ?: e.message()
            } catch (parseError: Exception) {
                "An unknown error occurred"
            }
            Resource.Error(message = errorMessage ?: "Server Error")
        }catch (e: IOException){
            Resource.Error(message = "No Internet Connection")
        }catch (e: Exception){
            Resource.Error(message = e.message ?: "An unknown error occurred")
        }
    }

    override suspend fun deleteThreeD(threeDId: String): Resource<ThreeDDto> {
        return try {
            val response = api.deleteThreeD(threeDId)
            if (response.success && response.data != null){
                Resource.Success(response.data)
            }else{
                Resource.Error(message = response.message)
            }

        }catch (e: Exception){
            Resource.Error(message = e.message ?: "An unknow error occurred")
        }
    }
}