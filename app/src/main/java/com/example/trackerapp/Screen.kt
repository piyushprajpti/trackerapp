package com.example.trackerapp

sealed class Screen (val route: String) {

data object AuthGraph: Screen("authGraph")
data object HomeGraph: Screen("homeGraph")

data object LoginScreen: Screen("loginScreen")
data object RegisterScreen: Screen("registerScreen")
data object HomeScreen: Screen("homeScreen")
}