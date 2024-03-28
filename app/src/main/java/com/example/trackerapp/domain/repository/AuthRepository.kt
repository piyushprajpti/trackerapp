package com.example.trackerapp.domain.repository

import com.example.trackerapp.domain.model.OtpResponse
import com.example.trackerapp.domain.model.RegisterResponse
import com.example.trackerapp.util.Response

interface AuthRepository {

    suspend fun sendOTP(number: String): Response<Boolean>

    suspend fun verifyOTP(number: String, otp: String): Response<OtpResponse>

    suspend fun registerUser(
        number: String,
        name: String,
        firmName: String,
        vehicleNumber: String
    ): Response<RegisterResponse>

}