package com.example.trackerapp.data.service

import android.util.Log
import com.example.trackerapp.domain.model.authModels.RegisterRequest
import com.example.trackerapp.domain.model.authModels.SendOtpRequest
import com.example.trackerapp.domain.model.authModels.VerifyOtpRequest
import com.example.trackerapp.domain.service.AuthService
import com.example.trackerapp.util.BackendUrl
import io.ktor.client.HttpClient
import io.ktor.client.request.get
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
        val response =
            client.post("$BackendUrl/signup") {
                contentType(ContentType.Application.Json)
                setBody(SendOtpRequest(number))
            }
//        Log.d("TAG", "sendOTP: ${response.bodyAsText()}")
//        Log.d("TAG", "sendOTP: response")
        return response
    }

    override suspend fun verifyOTP(number: String, otp: String): HttpResponse {
        val response =
            client.post("$BackendUrl/otp/verify") {
                contentType(ContentType.Application.Json)
                setBody(VerifyOtpRequest(number, otp))
            }
        return response
    }

    override suspend fun registerUser(
        number: String,
        name: String,
        firmName: String,
        appId: String,
        signature: String,
        vehicleNumber: String
    ): HttpResponse {
        val response =
            client.post("$BackendUrl/registration") {
                contentType(ContentType.Application.Json)
                setBody(RegisterRequest(number, name, firmName, appId, signature, vehicleNumber))
            }

//        Log.d("TAG", "registerUser service: ${response.bodyAsText()}")
        return response
    }

    override suspend fun getFirmList(): HttpResponse {
        val response =
            client.get("$BackendUrl/getIdAllFirm")
        return response
    }

}