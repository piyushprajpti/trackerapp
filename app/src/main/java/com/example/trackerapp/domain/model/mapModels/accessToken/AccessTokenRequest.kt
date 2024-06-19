package com.example.trackerapp.domain.model.mapModels.accessToken

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccessTokenRequest(

    @SerialName("appid")
    val appid: String,

    @SerialName("time")
    val time: Int,

    @SerialName("signature")
    val signature: String
)
