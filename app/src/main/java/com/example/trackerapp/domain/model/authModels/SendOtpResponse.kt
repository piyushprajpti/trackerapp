package com.example.trackerapp.domain.model.authModels

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SendOtpResponse(
    @SerialName("message")
    val message: String,

    @SerialName("result")
    var user: Result? = null,

    @SerialName("otpres")
    val Otpres: Otpres? = null

)

@Serializable
data class Result(
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

@Serializable
data class Otpres (
    @SerialName("message")
    val message: String
)