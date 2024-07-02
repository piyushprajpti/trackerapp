package com.example.trackerapp.data.service

import android.util.Log
import com.example.trackerapp.domain.model.mapModels.accessToken.AccessTokenRequest
import com.example.trackerapp.domain.service.MapService
import com.example.trackerapp.util.MapUrl
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType

class MapServiceImpl(
    private val client: HttpClient
) : MapService {

    override suspend fun generateAccessToken(
        appId: String,
        signature: String
    ): HttpResponse {

        val unixTime = System.currentTimeMillis() / 1000

        val response = client.post("$MapUrl/auth/") {
            contentType(ContentType.Application.Json)
            setBody(AccessTokenRequest(appId, 1715850396, signature))
        }

        return response
    }

    override suspend fun getDeviceList(
        accessToken: String,
        atPage: Int,
        pageSize: Int,
        needCount: Boolean
    ): HttpResponse {
        val response =
            client.get("$MapUrl/device?accessToken=$accessToken&atPage=$atPage&pageSize=$pageSize&needCount=$needCount")
        return response
    }

    override suspend fun getLiveLocation(accessToken: String, imei: String): HttpResponse {
        val response =
            client.get("$MapUrl/device/location?accessToken=$accessToken&imei=$imei")
        return response
    }

    override suspend fun getHistoryPlayback(
        accessToken: String,
        imei: String,
        startTime: Long,
        endTime: Long
    ): HttpResponse {
        val response =
            client.get("$MapUrl/device/track/history?accessToken=$accessToken&imei=$imei&startTime=$startTime&endTime=$endTime")
        return response
    }

}