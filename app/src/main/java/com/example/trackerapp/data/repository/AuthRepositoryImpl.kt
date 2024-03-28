package com.example.trackerapp.data.repository

import android.util.Log
import com.example.trackerapp.domain.model.ErrorResponse
import com.example.trackerapp.domain.model.LoginResponse
import com.example.trackerapp.domain.model.OtpResponse
import com.example.trackerapp.domain.model.RegisterResponse
import com.example.trackerapp.domain.repository.AuthRepository
import com.example.trackerapp.domain.service.AuthService
import com.example.trackerapp.util.Response
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException

class AuthRepositoryImpl(
    private val service: AuthService
) : AuthRepository {
    override suspend fun sendOTP(number: String): Response<Boolean> {
        return try {
            service.sendOTP(number)
            Response.Success(true)
        } catch (e: ClientRequestException) {
            Response.Error(e.response.body<LoginResponse>().message)
        } catch (e: ServerResponseException) {
            Response.Error(e.response.body<LoginResponse>().message)
        } catch (E: Exception) {
            Response.Error("Something went wrong")
        }
    }

    override suspend fun verifyOTP(number: String, otp: String): Response<OtpResponse> {
        return try {
            val response = service.verifyOTP(number, otp)
            Response.Success(response.body<OtpResponse>())
        } catch (e: ClientRequestException) {
            Response.Error(e.response.body<ErrorResponse>().message)
        } catch (e: ServerResponseException) {
            Response.Error(e.response.body<ErrorResponse>().message)
        } catch (E: Exception) {
            Response.Error("Something went wrong")
        }
    }

    override suspend fun registerUser(
        number: String,
        name: String,
        firmName: String,
        vehicleNumber: String
    ): Response<RegisterResponse> {
        return try {
            val response = service.registerUser(number, name, firmName, vehicleNumber)
            Response.Success(response.body<RegisterResponse>())
        } catch (e: ClientRequestException) {
            Response.Error(e.response.body<RegisterResponse>().message)
        } catch (e: ServerResponseException) {
            Response.Error(e.response.body<RegisterResponse>().message)
        } catch (E: Exception) {
            Response.Error("Something went wrong")
        }
    }
}