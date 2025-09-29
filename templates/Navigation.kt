package com.example.rommy_100

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

object AppDestinations {
    const val HOME_ROUTE = "home"
    const val ASSISTANT_ROUTE = "assistant"
    const val WELCOME_ROUTE = "welcome"
    const val REGISTER_ROUTE = "register"
    const val LOGIN_ROUTE = "login"
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppDestinations.WELCOME_ROUTE // La pantalla inicial
    ) {
        // Definición de los destinos (pantallas)
        composable(route = AppDestinations.HOME_ROUTE) {
            MainScreen(navController = navController)
        }

        composable(route = AppDestinations.ASSISTANT_ROUTE) {
            AsisstantScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(route = AppDestinations.REGISTER_ROUTE) {
            RegisterScreen(
                onNavigateBack = { navController.navigate(AppDestinations.WELCOME_ROUTE) },
                onRegisterClick = { fullName, email, password ->
                    navController.navigate(AppDestinations.HOME_ROUTE)
                },
                onLoginClick = { navController.navigate(AppDestinations.LOGIN_ROUTE) }
            )
        }

        composable(route = AppDestinations.LOGIN_ROUTE) {
            LoginScreen(
                onNavigateBack = { navController.navigate(AppDestinations.WELCOME_ROUTE) },
                onLoginClick = { email, password ->
                    navController.navigate(AppDestinations.HOME_ROUTE)
                },
                onRegisterClick = { navController.navigate(AppDestinations.REGISTER_ROUTE) },
                onForgotPasswordClick = {}
            )
        }

        composable(route = AppDestinations.WELCOME_ROUTE) {
            WelcomeScreen(
                onRegisterClick = { navController.navigate(AppDestinations.REGISTER_ROUTE) },
                onLoginClick = { navController.navigate(AppDestinations.LOGIN_ROUTE) },
                onForgotPasswordClick = {}
            )
        }
    }
}