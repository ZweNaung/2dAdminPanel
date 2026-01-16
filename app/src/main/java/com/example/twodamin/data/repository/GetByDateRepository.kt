package com.example.twodamin.data.repository


import com.example.twodamin.data.remote.dto.ChildUpdateRequestBody
import com.example.twodamin.data.remote.dto.TheHoleDayResultResponseDto
import com.example.twodamin.data.remote.dto.UpdateChildResultResponse

interface GetByDateRepository {
    suspend fun getByDate(date: String): TheHoleDayResultResponseDto

    suspend fun updateChildResult(date: String,childId: String,requestBody: ChildUpdateRequestBody): UpdateChildResultResponse
}