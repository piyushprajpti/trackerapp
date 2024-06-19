package com.example.trackerapp.data.repository

import android.util.Log
import com.example.trackerapp.domain.model.mapModels.accessToken.AccessTokenResponse
import com.example.trackerapp.domain.model.mapModels.deviceList.DeviceListResponse
import com.example.trackerapp.domain.model.mapModels.firmList.FirmListResponse
import com.example.trackerapp.domain.model.mapModels.historyPlayback.HistoryPlaybackResponse
import com.example.trackerapp.domain.model.mapModels.liveLocation.LiveLocationResponse
import com.example.trackerapp.domain.repository.MapRepository
import com.example.trackerapp.domain.service.MapService
import com.example.trackerapp.util.Response
import io.ktor.client.call.body

class MapRepositoryImpl(
    private val service: MapService
) : MapRepository {

    override suspend fun generateAccessToken(
        appId: String,
        signature: String
    ): Response<AccessTokenResponse> {
        try {

            val response = service.generateAccessToken(appId, signature)
            return Response.Success(response.body())

        } catch (e: Exception) {
            return Response.Error("Something went wrong. Please restart the app")
        }
    }



    override suspend fun getDeviceList(
        accessToken: String,
        atPage: Int,
        pageSize: Int,
        needCount: Boolean
    ): Response<DeviceListResponse> {
        try {
            val response = service.getDeviceList(accessToken, atPage, pageSize, needCount)
            return Response.Success(response.body())
        } catch (e: Exception) {
            return Response.Error("Internal error occurred.")
        }
    }

    override suspend fun getLiveLocation(
        accessToken: String,
        imei: String
    ): Response<LiveLocationResponse> {
        try {
            val response = service.getLiveLocation(accessToken, imei)
            return Response.Success(response.body())
        } catch (e: Exception) {
            return Response.Error("Internal error occurred.")
        }
    }

    override suspend fun getHistoryPlayback(
        accessToken: String,
        imei: String,
        startTime: Long,
        endTime: Long
    ): Response<HistoryPlaybackResponse> {
        try {
            val response = service.getHistoryPlayback(accessToken, imei, startTime, endTime)
            return Response.Success(response.body())
        } catch (e: Exception) {
            Log.d("TAG", "getHistoryPlayback: $e")
            return Response.Error("Something went wrong. Please try again.")
        }
    }

}