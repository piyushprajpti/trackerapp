package com.example.trackerapp.data.service

import com.example.trackerapp.domain.service.HomeService
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

class HomeServiceImpl(
    private val client: HttpClient
): HomeService {
    override suspend fun FetchUserInfo(userId: String): HttpResponse {

        return client.get("https://vehicle-trackingnpm-run-server.onrender.com/api/v1/auth/registration/${userId}")
    }
}