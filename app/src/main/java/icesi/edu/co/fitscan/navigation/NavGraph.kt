package icesi.edu.co.fitscan.navigation

import android.util.Log
import androidx.compose.runtime.Composable
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
import icesi.edu.co.fitscan.features.nutrition.ui.screens.NutritionPlanListScreen
import icesi.edu.co.fitscan.features.profile.ui.screens.ProfileScreen
import icesi.edu.co.fitscan.features.settings.ui.screens.SettingsScreen
import icesi.edu.co.fitscan.features.statistics.ui.screens.ExerciseProgressScreen
import icesi.edu.co.fitscan.features.statistics.ui.screens.ExerciseStatisticsScreen
import icesi.edu.co.fitscan.features.workout.ui.screens.CreateWorkoutScreen
import icesi.edu.co.fitscan.features.workout.ui.screens.ExerciseDetailScreen
import icesi.edu.co.fitscan.features.workout.ui.screens.PerformWorkoutScreen
import icesi.edu.co.fitscan.features.workout.ui.viewmodel.factory.ExerciseDetailViewModelFactory
import icesi.edu.co.fitscan.features.workoutlist.ui.screens.WorkoutListScreen
import icesi.edu.co.fitscan.ui.theme.greenLess

@Composable
fun NavigationHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Home.route) {
            DashboardScreen(navController = navController)
        }

        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }

        composable(Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }

        composable(Screen.Notifications.route) {
            NotificationsScreen(navController = navController)
        }

        composable(Screen.Workouts.route) {
            WorkoutListScreen(
                onNavigateToCreate = { navController.navigate(Screen.PerformWorkout.route) },
                onNavigateToPerform = { workoutId ->
                    navController.navigate("workout_detail/$workoutId")
                }
            )
        }

        composable(Screen.Meal.route) {
            NutritionPlanListScreen(/* Pasa parámetros si necesita */)
        }

        composable(Screen.Statistics.route) {
            ExerciseStatisticsScreen(navController = navController)
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
                },
            )
        }

        composable(Screen.ExerciseProgress.route) {
            ExerciseProgressScreen(navController = navController)
        }

        composable(Screen.CreateWorkout.route) {
            CreateWorkoutScreen()
        }

        composable(Screen.PerformWorkout.route) { backStackEntry ->
            //val workoutSessionId = backStackEntry.arguments?.getString("workoutSessionId")
            val workoutSessionId = "b48b68ba-1863-4ca7-87f7-5b32a5f4414e"
            PerformWorkoutScreen(workoutId = workoutSessionId)
        }

        composable(
            route = "workout_detail/{workoutId}",
            arguments = listOf(
                navArgument("workoutId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val workoutId = backStackEntry.arguments?.getString("workoutId")
            Log.d("NavGraph", "Navegando a workout_detail con workoutId: $workoutId")
            icesi.edu.co.fitscan.features.workout.ui.screens.WorkoutDetailScreen(
                workoutId = workoutId,
                onExerciseClick = { workoutId, workoutExerciseId ->
                    navController.navigate("exercise_detail/$workoutId/$workoutExerciseId")
                }
            )
        }

        composable(
            route = "exercise_detail/{workoutId}/{workoutExerciseId}",
            arguments = listOf(
                navArgument("workoutId") { type = NavType.StringType },
                navArgument("workoutExerciseId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val workoutId = backStackEntry.arguments?.getString("workoutId")
            val workoutExerciseId = backStackEntry.arguments?.getString("workoutExerciseId")
            ExerciseDetailScreen(
                workoutExerciseId = workoutExerciseId,
                workoutId = workoutId,
                onNavigateBack = { navController.popBackStack() },
                viewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = ExerciseDetailViewModelFactory())
            )
        }
    }
}