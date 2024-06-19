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
        startDestination = Screen.HomeGraph.route,
        enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
        exitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
    ) {

        // 1. Authentication Graph
        navigation(route = Screen.AuthGraph.route, startDestination = Screen.RegisterScreen.route) {

            composable(route = Screen.LoginScreen.route) {
                LoginScreen(
                    onRegistrationRedirect = { number -> navController.navigate(Screen.RegisterScreen.route + "?number=$number") },
                    onHomeRedirect = { navController.navigate(Screen.HomeScreen.route) }
                )
            }

            composable(
                route = Screen.RegisterScreen.route /*+ "?number={number}"*/,
                arguments = listOf(
                    navArgument("number") {
                        type = NavType.StringType
                    }
                )
            ) {
                val number = it.arguments?.getString("number")

                RegisterScreen(
                    number = number,
                    onRegisterSuccess = { navController.navigate(Screen.HomeScreen.route) }
                )
            }
        }

        // 2. Home Graph
        navigation(route = Screen.HomeGraph.route, startDestination = Screen.HomeScreen.route) {
            composable(route = Screen.HomeScreen.route) {
                HomeScreen(
                    authRedirect = {
                        navController.navigate(Screen.AuthGraph.route)
                    }
                )
            }
        }
    }
}