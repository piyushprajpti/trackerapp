package com.example.trackerapp.domain.model.mapModels.historyPlayback

import kotlinx.serialization.SerialName

data class PlaybackData (
    val code: Int,

    val imei: String,

    val lat: String,

    val lng: String,

    val speed: Int,

    val course: Int,

    val gpsTime: Int,

    val positionType: String
)
