package com.example.trackerapp.domain.model.authModels

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OtpRequest(
    @SerialName("number")
    val number: String,

    @SerialName("otp")
    val otp: String
)