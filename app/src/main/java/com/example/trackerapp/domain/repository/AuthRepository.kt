package com.example.trackerapp.domain.repository

import com.example.trackerapp.util.Response

interface AuthRepository {

    suspend fun sendOTP(number: String) : Response<Boolean>

    suspend fun verifyOTP(otp: String): Response<Boolean>

}