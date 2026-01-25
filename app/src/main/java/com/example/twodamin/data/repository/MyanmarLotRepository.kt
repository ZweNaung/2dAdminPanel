package com.example.twodamin.data.repository

import com.example.twodamin.data.remote.dto.MMLotResponseDto
import com.example.twodamin.util.Resource
import java.io.File

interface MyanmarLotRepository {
    // Get All
    suspend fun getAllMyanmarLots(): Resource<List<MMLotResponseDto>>

    // Upload (File နဲ့ Name ပဲ လက်ခံမယ်၊ Multipart conversion ကို impl မှာလုပ်မယ်)
    suspend fun uploadMyanmarLot(file: File, name: String): Resource<MMLotResponseDto>

    // Delete
    suspend fun deleteMyanmarLot(id: String): Resource<String>
}