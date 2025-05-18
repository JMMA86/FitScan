package icesi.edu.co.fitscan.features.workout.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun WorkoutListScreen(

    navController: NavHostController
) {
    Column {
        Text(text="Rutinas")
        // Demo: Navegar a la primera rutina
        Button(onClick = { navController.navigate("workout_detail/1") }) {
            Text("Ver Detalle de Rutina (demo)")
        }
    }
}