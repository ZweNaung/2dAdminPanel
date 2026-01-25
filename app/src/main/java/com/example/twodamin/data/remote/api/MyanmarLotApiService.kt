package com.example.twodamin.data.remote.api

import com.example.twodamin.data.remote.dto.MMLotResponseDto
import com.example.twodamin.data.remote.response.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface MyanmarLotApiService {

    // 1. GET All
    @GET("myanmarLot/all")
    suspend fun getAllMyanmarLots(): BaseResponse<List<MMLotResponseDto>>

    // 2. POST (Upload Image & Name)
    // backend မှာ upload.single('myanmarLotImg') လို့ပေးထားလို့ part name က အရေးကြီးပါတယ်
    @Multipart
    @POST("myanmarLot/")
    suspend fun uploadMyanmarLot(
        @Part image: MultipartBody.Part,
        @Part("name") name: RequestBody
    ): BaseResponse<MMLotResponseDto>

    // 3. DELETE
    @DELETE("myanmarLot/{id}")
    suspend fun deleteMyanmarLot(
        @Path("id") id: String
    ): BaseResponse<MMLotResponseDto> // Data ပြန်မပါလာနိုင်တဲ့အတွက် Any? သို့မဟုတ် Unit သုံးပါတယ်
}