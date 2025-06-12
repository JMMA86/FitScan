package icesi.edu.co.fitscan.navigation

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import icesi.edu.co.fitscan.features.auth.ui.screens.LoginScreen
import icesi.edu.co.fitscan.features.auth.ui.screens.PersonalDataScreen
import icesi.edu.co.fitscan.features.auth.ui.screens.RegisterScreen
import icesi.edu.co.fitscan.features.home.ui.screens.DashboardScreen
import icesi.edu.co.fitscan.features.mealPlan.ui.screens.MealPlanListScreen
import icesi.edu.co.fitscan.features.mealPlan.ui.screens.CreateMealPlanScreen
import icesi.edu.co.fitscan.features.nutrition.ui.screens.NutritionPlanListScreen
import icesi.edu.co.fitscan.features.statistics.ui.screens.DetailedChartsScreen
import icesi.edu.co.fitscan.features.statistics.ui.screens.ExerciseProgressScreen
import icesi.edu.co.fitscan.features.statistics.ui.screens.MuscleGroupProgressScreen
import icesi.edu.co.fitscan.features.statistics.ui.screens.ProgressPhotoScreen
import icesi.edu.co.fitscan.features.statistics.ui.screens.StatisticsScreen
import icesi.edu.co.fitscan.features.workout.ui.screens.CreateWorkoutScreen
import icesi.edu.co.fitscan.features.workout.ui.screens.ExerciseDetailScreen
import icesi.edu.co.fitscan.features.workout.ui.screens.PerformWorkoutScreen
import icesi.edu.co.fitscan.features.workout.ui.viewmodel.factory.ExerciseDetailViewModelFactory
import icesi.edu.co.fitscan.features.workoutlist.ui.screens.WorkoutListScreen
import icesi.edu.co.fitscan.features.workout.ui.screens.WorkoutDetailScreen
import icesi.edu.co.fitscan.features.mealPlan.ui.screens.MealPlanDetailScreen


@Composable
fun NavigationHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(300)
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(300)
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(300)
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(300)
            )
        }
    ) {
        composable(Screen.Home.route) {
            DashboardScreen(navController = navController)
        }

        composable(Screen.Workouts.route) {
            WorkoutListScreen(
                onNavigateToCreate = { navController.navigate(Screen.CreateWorkout.route) },
                onNavigateToPerform = { workoutId ->
                    navController.navigate("workout_detail/$workoutId")
                }
            )
        }

        composable(Screen.Meal.route) {
            MealPlanListScreen(navController = navController)
        }

        composable("create_meal_plan") {
            CreateMealPlanScreen(navController = navController)
        }

        composable(Screen.Statistics.route) {
            StatisticsScreen(navController = navController)
        }

        composable(
            route = "${Screen.Login.route}?message={message}",
            arguments = listOf(
                navArgument("message") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                }
            )        ) {
            LoginScreen(
                onLoginSuccess = {
                    // Navigate to Home (Dashboard) and clear the history up to Login
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Registration.route)
                }
            )
        }

        composable(Screen.Registration.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.BodyMeasurements.route) {
                        popUpTo(Screen.Registration.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack(Screen.Login.route, false)
                }
            )
        }

        composable(Screen.BodyMeasurements.route) {
            PersonalDataScreen(
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
            CreateWorkoutScreen(navController = navController)
        }

        composable(
            route = Screen.PerformWorkout.route,
            arguments = listOf(navArgument("workoutId") { type = NavType.StringType })
        ) { backStackEntry ->
            val workoutId = backStackEntry.arguments?.getString("workoutId")
            PerformWorkoutScreen(
                workoutId = workoutId.toString(),
                onFinishWorkout = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToExerciseDetail = { workoutId, workoutExerciseId ->
                    navController.navigate("exercise_detail/$workoutId/$workoutExerciseId") {
                        // No usar popUpTo para mantener el PerformWorkoutScreen en el stack
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = "workout_detail/{workoutId}",
            arguments = listOf(
                navArgument("workoutId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val workoutId = backStackEntry.arguments?.getString("workoutId")
            Log.d("NavGraph", "Navegando a workout_detail con workoutId: $workoutId")
            WorkoutDetailScreen(
                workoutId = workoutId,
                onExerciseClick = { workoutId, workoutExerciseId ->
                    navController.navigate("exercise_detail/$workoutId/$workoutExerciseId")
                },
                onStartWorkout = {
                    workoutId?.let { navController.navigate("perform_workout/$it") }
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
                viewModel = viewModel(factory = ExerciseDetailViewModelFactory())            )
        }
          composable(Screen.DetailedCharts.route) {
            DetailedChartsScreen(navController = navController)
        }

        composable(Screen.VisualProgress.route) {
            ProgressPhotoScreen(navController = navController)
        }

        composable(Screen.MuscleGroupProgress.route) {
            MuscleGroupProgressScreen(navController = navController)
        }

        composable("nutrition_plan_list") {
            NutritionPlanListScreen()
        }

        composable("meal_plan_detail/{mealPlanId}") { backStackEntry ->
            val mealPlanId = backStackEntry.arguments?.getString("mealPlanId") ?: ""
            MealPlanDetailScreen(mealPlanId = mealPlanId)
        }
    }
}