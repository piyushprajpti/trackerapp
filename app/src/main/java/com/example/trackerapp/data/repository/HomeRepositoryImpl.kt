package com.example.trackerapp.data.repository

import android.util.Log
import com.example.trackerapp.domain.model.homeModels.GetUserInfoResponse
import com.example.trackerapp.domain.repository.HomeRepository
import com.example.trackerapp.domain.service.HomeService
import com.example.trackerapp.util.Response
import io.ktor.client.call.body

class HomeRepositoryImpl(
    private val service: HomeService
): HomeRepository {
    override suspend fun getUserInfo(number: String): Response<GetUserInfoResponse> {
        try {
            val response = service.getUserInfo(number)
            return Response.Success(response.body<GetUserInfoResponse>())
        } catch (e: Exception) {
            return Response.Error("Unable to fetch user details")
        }
    }
}