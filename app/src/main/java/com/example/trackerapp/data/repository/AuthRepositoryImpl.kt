package com.example.trackerapp.data.repository

import android.util.Log
import com.example.trackerapp.domain.model.authModels.ErrorResponse
import com.example.trackerapp.domain.model.authModels.RegisterResponse
import com.example.trackerapp.domain.model.authModels.SendOtpResponse
import com.example.trackerapp.domain.model.authModels.VerifyOtpResponse
import com.example.trackerapp.domain.model.mapModels.firmList.FirmListResponse
import com.example.trackerapp.domain.repository.AuthRepository
import com.example.trackerapp.domain.service.AuthService
import com.example.trackerapp.util.Response
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.bodyAsText

class AuthRepositoryImpl(
    private val service: AuthService
) : AuthRepository {
    override suspend fun sendOTP(number: String): Response<SendOtpResponse> {
        return try {
            val response = service.sendOTP(number)
            Response.Success(response.body())
        } catch (e: ClientRequestException) {
            Response.Error("e.response.body<SendOtpResponse>().message")
        } catch (e: ServerResponseException) {
            Response.Error("Error sending OTP: Authenticate")
        } catch (e: Exception) {
            Log.d("TAG", "sendOTP: ${e.message}")
            Response.Error("Something went wrong")
        }
    }

    override suspend fun verifyOTP(number: String, otp: String): Response<VerifyOtpResponse> {
        return try {
            val response = service.verifyOTP(number, otp)
            Response.Success(response.body<VerifyOtpResponse>())
        } catch (e: ClientRequestException) {
            Response.Error(e.response.body<ErrorResponse>().message)
        } catch (e: ServerResponseException) {
            Response.Error("Server down. Please try again later")
        } catch (e: Exception) {
            Log.d("TAG", "sendOTP: ${e.message}")
            Response.Error("Something went wrong")
        }
    }

    override suspend fun registerUser(
        number: String,
        name: String,
        firmName: String,
        appId: String,
        signature: String,
        vehicleNumber: String
    ): Response<RegisterResponse> {
        return try {
            val response =
                service.registerUser(number, name, firmName, appId, signature, vehicleNumber)
            Log.d("TAG", "registerUser try: ${response.bodyAsText()}")
            Response.Success(response.body<RegisterResponse>())
        } catch (e: ClientRequestException) {
            Response.Error(e.response.body<RegisterResponse>().message)
        } catch (e: ServerResponseException) {
            Log.d("TAG", "registerUser catch: ${e}")
            Response.Error("Server down. Please try again later")
        } catch (e: Exception) {
            Log.d("TAG", "registerUser exception: $e")
            Response.Error("Something went wrong. Please click register again")
        }
    }

    override suspend fun getFirmList(): Response<FirmListResponse> {
        try {
            val response = service.getFirmList()
            return Response.Success(response.body())
        } catch (e: Exception) {
            return Response.Error("Unable to fetch firm list")
        }

    }
}