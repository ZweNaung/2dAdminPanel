package com.example.twodamin.data.repository

import com.example.twodamin.data.remote.dto.ChildUpdateRequestBody
import com.example.twodamin.data.remote.dto.TheHoleDayResultResponseDto
import com.example.twodamin.data.remote.dto.UpdateChildResultResponse
import com.example.twodamin.data.remote.api.DailyResultApiService

class GetByDateRepositoryImpl(private val dailyResultApiService: DailyResultApiService) : GetByDateRepository{
    override suspend fun getByDate(date: String): TheHoleDayResultResponseDto {
        return dailyResultApiService.getByDate(date)
    }

    override suspend fun updateChildResult(
        date: String,
        childId: String,
        requestBody: ChildUpdateRequestBody,
    ): UpdateChildResultResponse {
        return dailyResultApiService.updateChildResult(
            date = date,
            childId = childId,
            requestBody = requestBody
        )
    }
}