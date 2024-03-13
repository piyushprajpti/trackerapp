package com.example.trackerapp

sealed class Screen (val route: String) {
data object LoginScreen: Screen("loginScreen")
data object RegisterScreen: Screen("registerScreen")
}