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
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) { DashboardScreen() }
        composable(Screen.Workouts.route) { WorkoutListScreen() }
        composable(Screen.Login.route) { LoginScreen(
            Color(0xFF4CAF50),
            navController=navController
        ) }
        composable(Screen.Registration.route) { RegisterScreen(Color(0xFF4CAF50)) }
    }
}