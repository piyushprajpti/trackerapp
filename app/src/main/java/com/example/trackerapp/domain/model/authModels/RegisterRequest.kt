package com.example.trackerapp.domain.model.authModels

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    @SerialName("number")
    val number: String,

    @SerialName("name")
    val name: String,

    @SerialName("firm")
    val firmName: String,

    @SerialName("vehicleNumber")
    val vehicleNumber: String,

    @SerialName("state")
    val state: String = "",

    @SerialName("district")
    val district: String = "",

    @SerialName("cityOrVillage")
    val cityOrVillage: String = ""

)