package com.example.trackerapp.di

import com.example.trackerapp.data.repository.AuthRepositoryImpl
import com.example.trackerapp.data.service.AuthServiceImpl
import com.example.trackerapp.domain.repository.AuthRepository
import com.example.trackerapp.domain.service.AuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Singleton
    @Provides
    fun providesHttpClient(): HttpClient {
        return HttpClient(Android) {
            expectSuccess = true
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }

    @Singleton
    @Provides
    fun providesAuthService(client: HttpClient): AuthService {
        return AuthServiceImpl(client)
    }

    @Singleton
    @Provides
    fun providesAuthRepository(service: AuthService): AuthRepository {
        return AuthRepositoryImpl(service)
    }
}