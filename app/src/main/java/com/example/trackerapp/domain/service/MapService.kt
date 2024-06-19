package com.example.trackerapp.domain.service

import io.ktor.client.statement.HttpResponse

interface MapService {

    suspend fun generateAccessToken(
        appId: String,
        signature: String
    ): HttpResponse


    suspend fun getDeviceList(
        accessToken: String,
        atPage: Int,
        pageSize: Int,
        needCount: Boolean
    ): HttpResponse

    suspend fun getLiveLocation(
        accessToken: String,
        imei: String
    ): HttpResponse

    suspend fun getHistoryPlayback(
        accessToken: String,
        imei: String,
        startTime: Long,
        endTime: Long
    ): HttpResponse
}