package com.example.trackerapp.data.service

import com.example.trackerapp.domain.service.AuthService
import kotlinx.coroutines.delay

class AuthServiceImpl(

) : AuthService {
    override suspend fun sendOTP() : Boolean {
        delay(2000)
        return true
        //Post Request
    }

    override suspend fun verifyOTP() {
        TODO("Not yet implemented")
    }
}