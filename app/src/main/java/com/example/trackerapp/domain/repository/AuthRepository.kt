package com.example.trackerapp.domain.repository

import com.example.trackerapp.util.Response

interface AuthRepository {

    suspend fun sendOTP(number: String) : Response<Boolean>

    suspend fun verifyOTP(number: String, otp: String): Response<Boolean>

}