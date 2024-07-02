package com.example.trackerapp.domain.service

import io.ktor.client.statement.HttpResponse

interface AuthService {

    suspend fun sendOTP(number: String): HttpResponse

    suspend fun verifyOTP(number: String, otp: String): HttpResponse

    suspend fun registerUser(
        number: String,
        name: String,
        firmName: String,
        appId: String,
        signature: String,
        vehicleNumber: String
    ): HttpResponse

    suspend fun getFirmList(): HttpResponse
}