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
import icesi.edu.co.fitscan.features.workout.ui.screens.CreateWorkoutScreen
import icesi.edu.co.fitscan.features.workout.ui.screens.ExerciseDetailScreen
import icesi.edu.co.fitscan.features.workout.ui.screens.WorkoutDetailScreen
import icesi.edu.co.fitscan.features.workout.ui.screens.WorkoutListScreen
import icesi.edu.co.fitscan.ui.theme.greenLess

@Composable
fun NavigationHost(navController: NavHostController) {

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
            WorkoutListScreen(navController)
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
                    // Navigate to Home (Dashboard) and clear the history up to Login
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
                    // Navigate to body measurements screen after registration
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
                    // Navigate to Home screen after measurements are saved
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
            )
        }
        // --- Workout Detail Screen (optional: add if you want to navigate to a specific workout) ---
        composable(
            route = "workout_detail/{workoutId}",
            arguments = listOf(navArgument("workoutId") { type = NavType.StringType })
        ) { backStackEntry ->
            val workoutId = backStackEntry.arguments?.getString("workoutId")
            WorkoutDetailScreen(
                workoutId = workoutId,
                onNavigateBack = { navController.popBackStack() },
                onExerciseClick = { workoutExerciseId ->
                    navController.navigate("exercise_detail/$workoutExerciseId")
                }
            )
        }
        // --- Exercise Detail Screen ---
        composable(
            route = "exercise_detail/{workoutExerciseId}",
            arguments = listOf(navArgument("workoutExerciseId") { type = NavType.StringType })
        ) { backStackEntry ->
            val workoutExerciseId = backStackEntry.arguments?.getString("workoutExerciseId")
            ExerciseDetailScreen(
                workoutExerciseId = workoutExerciseId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}