package com.example.trackerapp.domain.model.authModels

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName("message")
    val message: String
)
