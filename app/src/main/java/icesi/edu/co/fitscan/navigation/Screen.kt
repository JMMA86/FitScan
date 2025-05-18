package icesi.edu.co.fitscan.navigation

import icesi.edu.co.fitscan.R

sealed class Screen(val route: String, val title: String, val icon: Int) {
    data object Home : Screen("home", "Inicio", R.drawable.ic_home)
    data object Workouts : Screen("workouts", "Rutinas", R.drawable.ic_fitness)
    data object Profile : Screen("profile", "Comidas", R.drawable.ic_cutlery)
    data object Settings : Screen("statistics", "Estadísticas", R.drawable.ic_statistics)
    data object Login : Screen("login", "Iniciar Sesión", R.drawable.ic_home)
    data object Registration: Screen("registration", "Registrarse", R.drawable.ic_home)
    data object BodyMeasurements : Screen("body_measurements", "Medidas Corporales", R.drawable.ic_home)
    data object CreateWorkout : Screen("create_workout", "Crear Rutina", R.drawable.ic_home)
}