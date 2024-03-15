package com.example.trackerapp.domain.repository

interface AuthRepository {

    suspend fun sendOTP() : Boolean

    suspend fun verifyOTP()

}