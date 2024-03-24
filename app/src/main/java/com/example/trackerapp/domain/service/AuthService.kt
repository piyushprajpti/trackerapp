package com.example.trackerapp.domain.service

import io.ktor.client.statement.HttpResponse

interface AuthService {

    suspend fun sendOTP(number: String) : HttpResponse

    suspend fun verifyOTP(number: String, otp: String): HttpResponse
}