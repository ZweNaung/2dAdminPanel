package com.example.twodamin.data.repository

import com.example.twodamin.data.remote.dto.ModernDto
import com.example.twodamin.util.Resource

interface ModernRepository {
        suspend fun updateData(title: String, modern: String, internet: String): Resource<ModernDto>
        suspend fun getData(title: String): Resource<ModernDto>
}