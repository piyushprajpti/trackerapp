package com.example.trackerapp.data.service

import com.example.trackerapp.domain.model.homeModels.GetUserInfoRequest
import com.example.trackerapp.domain.service.HomeService
import com.example.trackerapp.util.BackendUrl
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType

class HomeServiceImpl(
    private val client: HttpClient
) : HomeService {
    override suspend fun getUserInfo(number: String): HttpResponse {
        val response = client.post("$BackendUrl/loginGet") {
            contentType(ContentType.Application.Json)
            setBody(GetUserInfoRequest(number))
        }

        return response
    }
}