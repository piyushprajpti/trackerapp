package com.example.trackerapp.domain.service

import io.ktor.client.statement.HttpResponse

interface HomeService {
    suspend fun FetchUserInfo(userId: String): HttpResponse
}