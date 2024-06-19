package com.example.trackerapp.domain.model.mapModels.deviceList

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeviceListResponse(

    @SerialName("code")
    val code: Int,

    @SerialName("data")
    val data: List<DeviceInfo>,

    @SerialName("page")
    val page: Page
)

@Serializable
data class DeviceInfo(

    @SerialName("code")
    val code: Int,

    @SerialName("imei")
    val imei: String,

    @SerialName("mobile")
    val mobile: String,

    @SerialName("deviceName")
    val deviceName: String,

    @SerialName("isWireless")
    val isWireless: Int,

    @SerialName("deviceType")
    val deviceType: String,

    @SerialName("activateTime")
    val activateTime: Int,

    @SerialName("platformEndTime")
    val platformEndTime: Int,
)

@Serializable
data class Page (

    @SerialName("pageSize")
    val pageSize: Int,

    @SerialName("currentPage")
    val currentPage: Int,

    @SerialName("count")
    val count: Int,
)