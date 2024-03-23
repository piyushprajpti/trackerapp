package com.example.trackerapp.util

sealed class Response<out PLACEHOLDER>{
    data class Success<PLACEHOLDER>(val data: PLACEHOLDER) : Response<PLACEHOLDER>()

    data class Error<PLACEHOLDER>(val error: String) : Response<PLACEHOLDER>()
}