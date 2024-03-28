package com.example.trackerapp.util

sealed class Response<out T>{
    data class Success<T>(val data: T) : Response<T>()

    data class Error<T>(val error: String) : Response<T>()
}