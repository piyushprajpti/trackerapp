package com.example.trackerapp.domain.repository

import com.example.trackerapp.domain.model.mapModels.accessToken.AccessTokenResponse
import com.example.trackerapp.domain.model.mapModels.deviceList.DeviceListResponse
import com.example.trackerapp.domain.model.mapModels.firmList.FirmListResponse
import com.example.trackerapp.domain.model.mapModels.historyPlayback.HistoryPlaybackResponse
import com.example.trackerapp.domain.model.mapModels.liveLocation.LiveLocationResponse
import com.example.trackerapp.util.Response

interface MapRepository {

    suspend fun generateAccessToken(
        appId: String,
        signature: String
    ): Response<AccessTokenResponse>


    suspend fun getDeviceList(
        accessToken: String,
        atPage: Int,
        pageSize: Int,
        needCount: Boolean
    ): Response<DeviceListResponse>

    suspend fun getLiveLocation(
        accessToken: String,
        imei: String
    ): Response<LiveLocationResponse>

    suspend fun getHistoryPlayback(
        accessToken: String,
        imei: String,
        startTime: Long,
        endTime: Long
    ): Response<HistoryPlaybackResponse>

}