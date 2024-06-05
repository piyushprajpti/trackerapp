package com.example.trackerapp.di

import com.example.trackerapp.data.repository.HomeRepositoryImpl
import com.example.trackerapp.data.service.HomeServiceImpl
import com.example.trackerapp.domain.repository.HomeRepository
import com.example.trackerapp.domain.service.HomeService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeModule {

    @Singleton
    @Provides
    fun provideHomeService(client: HttpClient): HomeService {
        return HomeServiceImpl(client)
    }

    @Singleton
    @Provides
    fun provideHomeRepository(service: HomeService): HomeRepository {
        return HomeRepositoryImpl(service)
    }
}