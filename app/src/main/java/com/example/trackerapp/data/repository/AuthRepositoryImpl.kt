package com.example.trackerapp.data.repository

import com.example.trackerapp.domain.model.LoginResponse
import com.example.trackerapp.domain.model.OtpResponse
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

    override suspend fun verifyOTP(number: String, otp: String): Response<Boolean> {
        return try {
            service.verifyOTP(number, otp)
            Response.Success(true)
        } catch (e: ClientRequestException) {
            Response.Error(e.response.body<OtpResponse>().message)
        } catch (e: ServerResponseException) {
            Response.Error(e.response.body<OtpResponse>().message)
        } catch (E: Exception) {
            Response.Error("Something went wrong")
        }
    }
}