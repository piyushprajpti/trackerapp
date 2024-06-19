package com.example.trackerapp.domain.model.mapModels.accessToken

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccessTokenResponse(

    @SerialName("code")
    val code: Int,

    @SerialName("accessToken")
    val accessToken: String,

    @SerialName("expiresIn")
    val expiresIn: Int
)
