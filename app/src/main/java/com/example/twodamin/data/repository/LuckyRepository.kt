package com.example.twodamin.data.repository

import com.example.twodamin.data.remote.dto.ViewAllLuckyDto

interface LuckyRepository{
    suspend fun getAllLucky(): ViewAllLuckyDto
}