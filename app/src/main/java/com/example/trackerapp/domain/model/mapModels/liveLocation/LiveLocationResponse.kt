package com.example.trackerapp.domain.model.mapModels.liveLocation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LiveLocationResponse(

    @SerialName("code")
    val code: Int,

    @SerialName("lat")
    val lat: String,

    @SerialName("lng")
    val lng: String,

    @SerialName("gpsTime")
    val gpsTime: Int,

    @SerialName("address")
    val address: String
)
