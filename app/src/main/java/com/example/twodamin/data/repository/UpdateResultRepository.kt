package com.example.twodamin.data.repository

import com.example.twodamin.data.remote.dto.UpdateResultDto
import com.example.twodamin.util.Resource

interface UpdateResultRepository {
    suspend fun getUpdateResult(): Resource<List<UpdateResultDto>>

    suspend fun entryUpdateResult(body: UpdateResultDto): Resource<UpdateResultDto>
}