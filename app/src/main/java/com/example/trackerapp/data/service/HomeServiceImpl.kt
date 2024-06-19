package com.example.trackerapp.data.service

import com.example.trackerapp.domain.service.HomeService
import io.ktor.client.HttpClient

class HomeServiceImpl(
    private val client: HttpClient
) : HomeService {
}