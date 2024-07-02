package com.example.trackerapp.domain.service

import io.ktor.client.statement.HttpResponse

interface HomeService {
    suspend fun getUserInfo(number: String): HttpResponse
}