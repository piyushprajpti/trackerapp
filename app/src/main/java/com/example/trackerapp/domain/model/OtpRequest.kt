package com.example.trackerapp.domain.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class OtpRequest(
    @SerializedName("number")
    val number: String,

    @SerializedName("otp")
    val otp: String
)