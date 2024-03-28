package com.example.trackerapp

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
        navigation(route = "auth_graph", startDestination = Screen.LoginScreen.route) {

            composable(route = Screen.LoginScreen.route) {
                LoginScreen(
                    onRegistrationRedirect = { number -> navController.navigate(Screen.RegisterScreen.route + "?number=$number") },
                    onHomeRedirect = { navController.navigate(Screen.HomeScreen.route) }
                )
            }

            composable(
                route = Screen.RegisterScreen.route + "?number={number}",
                arguments = listOf(
                    navArgument("number") {
                        type = NavType.StringType
                    }
                )
            ) {
                RegisterScreen(
                    number = it.arguments?.getString("number"),
                    onRegisterSuccess = { navController.navigate(Screen.HomeScreen.route) }
                )
            }
        }

        // 2. Home Graph
        navigation(route = "home_graph", startDestination = Screen.HomeScreen.route) {
            composable(route = Screen.HomeScreen.route) {
                HomeScreen()
            }
        }
    }
}