package com.example.trackerapp.di

import com.example.trackerapp.data.repository.MapRepositoryImpl
import com.example.trackerapp.data.service.MapServiceImpl
import com.example.trackerapp.domain.repository.MapRepository
import com.example.trackerapp.domain.service.MapService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MapModule {

    @Singleton
    @Provides
    fun providesMapService(client: HttpClient): MapService {
        return MapServiceImpl(client)
    }

    @Singleton
    @Provides
    fun providesMapRepository(service: MapService): MapRepository {
        return MapRepositoryImpl(service)
    }

}