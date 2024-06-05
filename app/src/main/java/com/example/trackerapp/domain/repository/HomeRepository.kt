package com.example.trackerapp.domain.repository

import com.example.trackerapp.domain.model.homeModels.FetchUserInfoResponse
import retrofit2.Response

interface HomeRepository {
    suspend fun fetchUserInfo(): Response<FetchUserInfoResponse>
}