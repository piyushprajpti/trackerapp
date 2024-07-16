package com.example.trackerapp.domain.model.mapModels.historyPlayback

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HistoryPlaybackResponse(

    @SerialName("code")
    val code: Int,

    @SerialName("result")
    val result: String = "",

    @SerialName("data")
    val playbackLatLngList: List<PlaybackLatLngList> = emptyList()
)

@Serializable
data class PlaybackLatLngList (

    @SerialName("code")
    val code: Int,

    @SerialName("imei")
    val imei: String,

    @SerialName("lat")
    val lat: String,

    @SerialName("lng")
    val lng: String,

    @SerialName("speed")
    val speed: Int,

    @SerialName("course")
    val course: Long = 0,

    @SerialName("gpsTime")
    val gpsTime: Int,

    @SerialName("positionType")
    val positionType: String
)
