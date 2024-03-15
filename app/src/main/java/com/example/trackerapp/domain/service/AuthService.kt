package com.example.trackerapp.domain.service

interface AuthService {

    suspend fun sendOTP() : Boolean

    suspend fun verifyOTP()
}