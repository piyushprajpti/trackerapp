package com.example.trackerapp.data.service

import android.util.Log
import com.example.trackerapp.domain.model.LoginRequest
import com.example.trackerapp.domain.model.OtpRequest
import com.example.trackerapp.domain.model.RegisterRequest
import com.example.trackerapp.domain.service.AuthService
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthServiceImpl(
    private val client: HttpClient
) : AuthService {

    override suspend fun sendOTP(number: String): HttpResponse {
        val response = client.post("https://vehicle-tracking-gu26.onrender.com/api/v1/auth/signup") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(number))
            }
        return response
    }

    override suspend fun verifyOTP(number: String, otp: String): HttpResponse {
        val response = client.post("https://vehicle-tracking-gu26.onrender.com/api/v1/auth/otp/verify") {
                contentType(ContentType.Application.Json)
                setBody(OtpRequest(number, otp))
            }
        return response
    }

    override suspend fun registerUser(
        number: String,
        name: String,
        firmName: String,
        vehicleNumber: String
    ): HttpResponse {
        val response = client.post("https://vehicle-trackingnpm-run-server.onrender.com/api/v1/auth/registration") {
            contentType(ContentType.Application.Json)
            setBody(RegisterRequest(number, name, firmName, vehicleNumber))
        }
        return response
    }
}