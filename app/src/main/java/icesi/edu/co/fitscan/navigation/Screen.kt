package icesi.edu.co.fitscan.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Inicio", Icons.Filled.Home)
    object Workouts : Screen("workouts", "Entrenamiento", Icons.Filled.Home)
    object Profile : Screen("profile", "Nutrición", Icons.Filled.Home)
    object Settings : Screen("statistics", "Estadísticas", Icons.Filled.Home)
    object Login : Screen("login", "Iniciar Sesión", Icons.Filled.Home)
    object Registration: Screen("registration", "Registrarse", Icons.Filled.Home)
}