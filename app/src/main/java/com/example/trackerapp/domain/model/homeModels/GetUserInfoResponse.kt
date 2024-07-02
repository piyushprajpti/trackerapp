package com.example.trackerapp.domain.model.homeModels

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetUserInfoResponse(

    @SerialName("message")
    val message: String,

    @SerialName("profile")
    val profile: Profile? = null
)

@Serializable
data class Profile(

    @SerialName("_id")
    val id: String,

    @SerialName("number")
    val number: Long,

    @SerialName("name")
    val name: String,

    @SerialName("state")
    val state: String = "",

    @SerialName("district")
    val district: String = "",

    @SerialName("cityOrVillage")
    val cityOrVillage: String = "",

    @SerialName("firm")
    val firm: String,

    @SerialName("appId")
    val appId: String,

    @SerialName("signature")
    val signature: String,

    @SerialName("vehicleNumber")
    val vehicleNumber: String,

    @SerialName("createdAt")
    val createdAt: String,

    @SerialName("updatedAt")
    val updatedAt: String,

    @SerialName("__v")
    val version: Int,
)
