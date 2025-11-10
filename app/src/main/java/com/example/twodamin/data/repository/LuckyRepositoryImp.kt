package com.example.twodamin.data.repository

import com.example.twodamin.data.remote.api.LuckyApiService
import com.example.twodamin.data.remote.dto.ViewAllLuckyDto

class LuckyRepositoryImp(private val luckyApiService: LuckyApiService): LuckyRepository {
    override suspend fun getAllLucky(): ViewAllLuckyDto {
        return luckyApiService.getAllLucky()
    }
}