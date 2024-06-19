package com.example.trackerapp.data.repository

import android.util.Log
import com.example.trackerapp.domain.model.authModels.ErrorResponse
import com.example.trackerapp.domain.model.authModels.LoginResponse
import com.example.trackerapp.domain.model.authModels.OtpResponse
import com.example.trackerapp.domain.model.authModels.RegisterResponse
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
    override suspend fun sendOTP(number: String): Response<Boolean> {
        return try {
            service.sendOTP(number)
            Response.Success(true)
        } catch (e: ClientRequestException) {
            Response.Error(e.response.body<LoginResponse>().message)
        } catch (e: ServerResponseException) {
            Response.Error("Server down. Please try again later")
        } catch (e: Exception) {
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
            Response.Error("Server down. Please try again later")
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
            Log.d("TAG", "registerUser: ${response.bodyAsText()}")
            Response.Success(response.body<RegisterResponse>())
        } catch (e: ClientRequestException) {
            Response.Error(e.response.body<RegisterResponse>().message)
        } catch (e: ServerResponseException) {
            Log.d("TAG", "registerUser: ${e.response}")
            Response.Error("Server down. Please try again later")
        } catch (E: Exception) {
            Response.Error("Something went wrong")
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