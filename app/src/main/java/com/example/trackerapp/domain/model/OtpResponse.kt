package com.example.trackerapp.domain.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class OtpResponse(
    @SerializedName("message")
    val message: String
)