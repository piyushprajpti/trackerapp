package com.example.trackerapp.domain.model.mapModels.firmList

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FirmListResponse(

    @SerialName("message")
    val message: String,

    @SerialName("data")
    val FirmList: List<FirmList>


)

@Serializable
data class FirmList (

    @SerialName("firmName")
    val firmName: String,

    @SerialName("appId")
    val appid: String,

    @SerialName("signature")
    val signature: String,

    @SerialName("_id")
    val id: String = "",

    @SerialName("__v")
    val version: Int = 0
)
