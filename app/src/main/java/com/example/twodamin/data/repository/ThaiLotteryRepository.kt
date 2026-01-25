package com.example.twodamin.data.repository

import com.example.twodamin.data.remote.dto.ThaiLotResponseDto
import com.example.twodamin.util.Resource
import java.io.File

interface ThaiLotteryRepository {
    suspend fun getAllThaiLots(): Resource<List<ThaiLotResponseDto>>
    suspend fun uploadThaiLot(file: File, name: String): Resource<ThaiLotResponseDto>
    suspend fun deleteThaiLot(id: String): Resource<String>
}