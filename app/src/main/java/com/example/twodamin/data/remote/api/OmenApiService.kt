package com.example.twodamin.data.remote.api

import com.example.twodamin.data.remote.dto.OmenDeleteAllResponseDto
import com.example.twodamin.data.remote.dto.OmenDeleteResponseDto
import com.example.twodamin.data.remote.dto.OmenUpdateResponseDto
import com.example.twodamin.data.remote.dto.OmenUploadResponseDto
import com.example.twodamin.data.remote.dto.OmenViewAllResponseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

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

    //delete
    @DELETE("omen/{id}")
    suspend fun deleteOmen(@Path("id") omenId: String): OmenDeleteResponseDto

    //update
    @Multipart
    @PATCH("omen/{id}")
    suspend fun updateOmen(
        @Path("id") omenId: String,
        @Part("name") name: RequestBody?,
        @Part image: MultipartBody.Part?): OmenUpdateResponseDto

    // Delete All Omens
    @DELETE("omen/deleteall")
    suspend fun deleteAllOmens(): OmenDeleteAllResponseDto

}