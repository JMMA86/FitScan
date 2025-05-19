package icesi.edu.co.fitscan.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import icesi.edu.co.fitscan.features.auth.ui.screens.LoginScreen
import icesi.edu.co.fitscan.features.auth.ui.screens.PersonalDataScreen
import icesi.edu.co.fitscan.features.auth.ui.screens.RegisterScreen
import icesi.edu.co.fitscan.features.home.ui.screens.DashboardScreen
import icesi.edu.co.fitscan.features.notifications.ui.screens.NotificationsScreen
import icesi.edu.co.fitscan.features.profile.ui.screens.ProfileScreen
import icesi.edu.co.fitscan.features.settings.ui.screens.SettingsScreen
import icesi.edu.co.fitscan.features.workout.ui.screens.CreateWorkoutScreen
import icesi.edu.co.fitscan.features.workout.ui.screens.WorkoutListScreen
import icesi.edu.co.fitscan.ui.theme.greenLess

@Composable
fun NavigationHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Home.route) {
            DashboardScreen(navController = navController)
        }

        composable(Screen.Workouts.route) {
            WorkoutListScreen(
                onNavigateToCreate = { navController.navigate("create_workout") },
                onNavigateToPerform = { workoutId ->
                    navController.navigate("perform_workout/$workoutId")
                }
            )
        }

        composable(
            route = "${Screen.Login.route}?message={message}",
            arguments = listOf(
                navArgument("message") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val message = backStackEntry.arguments?.getString("message")
            LoginScreen(
                greenLess = greenLess,
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Registration.route)
                },
                onNavigateToForgotPassword = {
                    // Navigation to password recovery screen
                }
            )
        }

        composable(Screen.Registration.route) {
            RegisterScreen(
                greenLess = greenLess,
                onRegisterSuccess = {
                    navController.navigate(Screen.BodyMeasurements.route) {
                        popUpTo(Screen.Registration.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.BodyMeasurements.route) {
            PersonalDataScreen(
                greenLess = greenLess,
                onMeasurementsComplete = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable("profile") {
            ProfileScreen(navController = navController)
        }

        composable("settings") {
            SettingsScreen(navController = navController)
        }

        composable("notifications") {
            NotificationsScreen(navController = navController)
        }

        composable("create_workout") {
            CreateWorkoutScreen()
        }

        composable("perform_workout/{workoutId}") { backStackEntry ->
            val workoutId = backStackEntry.arguments?.getString("workoutId") ?: ""
            // Implement PerformWorkoutScreen navigation
        }
    }
}