package com.example.trackerapp.domain.model.authModels

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    @SerialName("success")
    val success: Boolean,

    @SerialName("message")
    val message: String,

    @SerialName("updateuser")
    val updateuser: Updateuser? = null,

    @SerialName("token")
    val token: String? = null
)

@Serializable
data class Updateuser(

    @SerialName("number")
    val number: Long,

    @SerialName("name")
    val name: String,

    @SerialName("firm")
    val firmName: String,

    @SerialName("appId")
    val appId: String,

    @SerialName("signature")
    val signature: String,

    @SerialName("vehicleNumber")
    val vehicleNumber: String,

    @SerialName("state")
    val state: String = "",

    @SerialName("district")
    val district: String = "",

    @SerialName("cityOrVillage")
    val cityOrVillage: String = "",

    @SerialName("_id")
    val _id: String,

    @SerialName("createdAt")
    val createdAt: String,

    @SerialName("updatedAt")
    val updatedAt: String,

    @SerialName("__v")
    val __v: Int
)