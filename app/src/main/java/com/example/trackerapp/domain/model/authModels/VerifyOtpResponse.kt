package com.example.trackerapp.domain.model.authModels

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerifyOtpResponse(
    @SerialName("message")
    val message: String,

    @SerialName("user")
    var user: User? = null,

    @SerialName("token")
    val token: String

)

@Serializable
data class User(
    @SerialName("_id")
    val id: String,

    @SerialName("number")
    val number: Long,

    @SerialName("otp")
    val otp: String,

    @SerialName("createdAt")
    val createdAt: String,

    @SerialName("updatedAt")
    val updatedAt: String,

    @SerialName("__v")
    val __v: Int

)