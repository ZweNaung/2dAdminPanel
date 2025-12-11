package com.example.twodamin.data.repository

import com.example.twodamin.data.remote.api.LuckyApiService
import com.example.twodamin.data.remote.dto.LuckyDeleteDto
import com.example.twodamin.data.remote.dto.LuckyDto
import com.example.twodamin.data.remote.response.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class LuckyRepositoryImp(private val luckyApiService: LuckyApiService): LuckyRepository {
    override suspend fun getAllLucky(): BaseResponse<List<LuckyDto>> {
        return luckyApiService.getAllLucky()
    }

    override suspend fun uploadLucky(
        name: RequestBody,
        section: RequestBody,
        image: MultipartBody.Part,

    ): BaseResponse<LuckyDto> {
        return luckyApiService.uploadLucky(
            name = name,
            section = section,
            image = image,
        )
    }

    override suspend fun deleteLucky(luckyId: String): BaseResponse<LuckyDeleteDto> {
        return luckyApiService.deleteLucky(
            luckyId = luckyId
        )
    }
}