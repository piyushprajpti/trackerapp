package com.example.trackerapp.domain.model.homeModels

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetUserInfoRequest (
    @SerialName("number")
    val number: String
)