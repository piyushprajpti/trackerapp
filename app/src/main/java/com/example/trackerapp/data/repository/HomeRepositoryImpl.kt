package com.example.trackerapp.data.repository

import com.example.trackerapp.domain.model.homeModels.FetchUserInfoResponse
import com.example.trackerapp.domain.repository.HomeRepository
import com.example.trackerapp.domain.service.HomeService
import retrofit2.Response

class HomeRepositoryImpl(
    private val service: HomeService
) : HomeRepository {
    override suspend fun fetchUserInfo(): Response<FetchUserInfoResponse> {
        TODO("Not yet implemented")
    }
}