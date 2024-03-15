package com.example.trackerapp.data.repository

import com.example.trackerapp.domain.repository.AuthRepository
import com.example.trackerapp.domain.service.AuthService

class AuthRepositoryImpl(
    private val service: AuthService
) : AuthRepository {
    override suspend fun sendOTP() : Boolean {
        // Business Logic
        // BUsa
        // sad
        val result = service.sendOTP()
        // Logic
        return result
    }

    override suspend fun verifyOTP() {
    }
}