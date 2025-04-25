package icesi.edu.co.fitscan.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import icesi.edu.co.fitscan.features.auth.ui.screens.LoginScreen
import icesi.edu.co.fitscan.features.auth.ui.screens.RegisterScreen
import icesi.edu.co.fitscan.features.home.ui.screens.DashboardScreen
import icesi.edu.co.fitscan.features.workout.ui.screens.WorkoutListScreen

@Composable
fun NavigationHost(navController: NavHostController) {

    // Define el color aquí o tómalo del Theme si es posible
    val greenLess = Color(0xFF4CAF50) // [cite: 1]

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
        // startDestination = Screen.Home.route
    ) {
        // --- Rutas Principales (requieren login) ---
        composable(Screen.Home.route) {
            DashboardScreen(/* Pasa parámetros si necesita */)
        }
        composable(Screen.Workouts.route) {
            WorkoutListScreen(/* Pasa parámetros si necesita */)
        }

        composable(Screen.Login.route) {
            LoginScreen(
                greenLess = greenLess,
                onLoginSuccess = {
                    // Navega a Home (Dashboard) y limpia el historial hasta Login
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                        launchSingleTop = true // Evita múltiples instancias de Home
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Registration.route) // Navega a Registro
                },
                onNavigateToForgotPassword = {
                    //Aqui me ponen las pantalals de recuperar contraseña
                    // navController.navigate("forgot_password_route")
                },
                onGoogleLoginClick = {
                    //
                }
            )
        }
        composable(Screen.Registration.route) {
            RegisterScreen(
                greenLess = greenLess
                // Añade callbacks si RegisterScreen los necesita (ej. onRegisterSuccess)
                // onRegisterSuccess = { navController.navigate(Screen.Login.route)
            )
        }
    }
}