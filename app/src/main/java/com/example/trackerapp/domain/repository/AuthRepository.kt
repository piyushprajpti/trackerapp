package com.example.trackerapp.domain.repository

import com.example.trackerapp.domain.model.authModels.OtpResponse
import com.example.trackerapp.domain.model.authModels.RegisterResponse
import com.example.trackerapp.domain.model.mapModels.firmList.FirmListResponse
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

    suspend fun getFirmList(): Response<FirmListResponse>

}