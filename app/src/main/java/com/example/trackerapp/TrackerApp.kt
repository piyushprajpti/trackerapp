package com.example.trackerapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.trackerapp.presentation.auth.LoginScreen
import com.example.trackerapp.presentation.auth.RegisterScreen
import com.example.trackerapp.presentation.home.HomeScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TrackerApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.HomeGraph.route,
        enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
        exitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
    ) {

        // 1. Authentication Graph
        navigation(route = Screen.AuthGraph.route, startDestination = Screen.LoginScreen.route) {

            composable(route = Screen.LoginScreen.route) {
                LoginScreen(
                    onRegistrationRedirect = {
                        navController.navigate(Screen.RegisterScreen.route)
                    },
                    onHomeRedirect = {
                        navController.navigate(Screen.HomeScreen.route) {
                            popUpTo(Screen.AuthGraph.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(route = Screen.RegisterScreen.route) {
                RegisterScreen(
                    onRegisterSuccess = {
                        navController.navigate(Screen.HomeScreen.route) {
                            popUpTo(Screen.AuthGraph.route) { inclusive = true }
                        }
                    }
                )
            }
        }

        // 2. Home Graph
        navigation(route = Screen.HomeGraph.route, startDestination = Screen.HomeScreen.route) {
            composable(route = Screen.HomeScreen.route) {
                HomeScreen(
                    authRedirect = {
                        navController.navigate(Screen.AuthGraph.route) {
                            popUpTo(Screen.HomeGraph.route) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}