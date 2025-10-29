package com.example.twodamin.data.remote.api

import com.example.twodamin.data.remote.dto.OmenUploadResponseDto
import com.example.twodamin.data.remote.dto.OmenViewAllResponseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface OmenApiService {
    //Get all
    @GET("omen/")
    suspend fun getAllOmen(): OmenViewAllResponseDto

    //Upload
    @Multipart
    @POST("omen/")
    suspend fun uploadOmen(
        @Part("name") name: RequestBody,
        @Part image: MultipartBody.Part
    ): OmenUploadResponseDto

//    //delete
//    @DELETE("omen/")
//    suspend fun deleteOmen():{}
}