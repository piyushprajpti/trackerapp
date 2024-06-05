package com.example.trackerapp.domain.model.authModels

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    @SerialName("number")
    val number: String
)
