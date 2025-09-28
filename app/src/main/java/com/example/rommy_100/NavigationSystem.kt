package com.example.rommy_100

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// Define tus rutas como constantes para evitar errores de tipeo
object AppDestinations {
    const val HOME_ROUTE = "home"
    const val ASSISTANT_ROUTE = "assistant"
    // Para rutas con argumentos: const val DETAIL_WITH_ARG_ROUTE = "detail/{itemId}"
}

@Composable
fun AppNavigation() {
    // 1. Crear el NavController
    val navController: NavHostController = rememberNavController()

    // 2. Definir el NavHost
    NavHost(
        navController = navController,
        startDestination = AppDestinations.HOME_ROUTE // La pantalla inicial
    ) {
        // 3. Definir los destinos (pantallas)
        composable(route = AppDestinations.HOME_ROUTE) {
            MainScreen(navController = navController)
        }

        composable(route = AppDestinations.ASSISTANT_ROUTE) {
            // Si necesitaras pasar el navController a DetailScreen, podrías hacerlo:
            // DetailScreen(navController = navController)
            // O si DetailScreen solo necesita volver atrás, puedes pasar una lambda:
            AsisstantScreen(onNavigateBack = { navController.popBackStack() })
        }

        // Ejemplo de ruta con argumento (opcional)
        // composable(route = "detail/{itemId}") { backStackEntry ->
        //     val itemId = backStackEntry.arguments?.getString("itemId")
        //     DetailScreenWithArgument(itemId = itemId, navController = navController)
        // }
    }
}