package com.example.trackerapp

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.trackerapp.presentation.auth.RegisterScreen
import com.example.trackerapp.presentation.auth.LoginScreen

@Composable
fun TrackerApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "auth_graph",
        enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
        exitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
    ) {

        // 1. Authentication Graph
        navigation(route = "auth_graph", startDestination = Screen.RegisterScreen.route) {

            composable(route = Screen.LoginScreen.route) {
                LoginScreen()
            }

            composable(route = Screen.RegisterScreen.route) {
                RegisterScreen()
            }
        }
    }
}