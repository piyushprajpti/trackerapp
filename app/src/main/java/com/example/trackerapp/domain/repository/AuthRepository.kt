package com.example.trackerapp.domain.repository

import com.example.trackerapp.domain.model.authModels.RegisterResponse
import com.example.trackerapp.domain.model.authModels.SendOtpResponse
import com.example.trackerapp.domain.model.authModels.VerifyOtpResponse
import com.example.trackerapp.domain.model.mapModels.firmList.FirmListResponse
import com.example.trackerapp.util.Response

interface AuthRepository {

    suspend fun sendOTP(number: String): Response<SendOtpResponse>

    suspend fun verifyOTP(number: String, otp: String): Response<VerifyOtpResponse>

    suspend fun registerUser(
        number: String,
        name: String,
        firmName: String,
        appId: String,
        signature: String,
        vehicleNumber: String
    ): Response<RegisterResponse>

    suspend fun getFirmList(): Response<FirmListResponse>

}