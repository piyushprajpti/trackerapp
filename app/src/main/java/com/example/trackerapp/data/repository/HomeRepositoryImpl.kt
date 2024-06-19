package com.example.trackerapp.data.repository

import com.example.trackerapp.domain.repository.HomeRepository
import com.example.trackerapp.domain.service.HomeService

class HomeRepositoryImpl(
    private val service: HomeService
): HomeRepository {
}