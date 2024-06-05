package com.example.trackerapp.domain.model.homeModels

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FetchUserInfoResponse(
    @SerialName("name")
    val name: String,

    @SerialName("number")
    val number: String
)
