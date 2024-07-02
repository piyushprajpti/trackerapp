package com.example.trackerapp.domain.repository

import com.example.trackerapp.domain.model.homeModels.GetUserInfoResponse
import com.example.trackerapp.util.Response

interface HomeRepository {
    suspend fun getUserInfo(number: String): Response<GetUserInfoResponse>
}