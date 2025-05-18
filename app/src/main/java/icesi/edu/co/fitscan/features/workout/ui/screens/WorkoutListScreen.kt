package icesi.edu.co.fitscan.features.workout.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import icesi.edu.co.fitscan.features.workout.ui.viewmodel.WorkoutListViewModel
import icesi.edu.co.fitscan.features.workout.ui.viewmodel.WorkoutUi
import icesi.edu.co.fitscan.features.workout.ui.viewmodel.WorkoutListUiState
import icesi.edu.co.fitscan.ui.theme.dashboardCardBackground
import icesi.edu.co.fitscan.ui.theme.dashboardGreen
import androidx.compose.ui.tooling.preview.Preview
import icesi.edu.co.fitscan.features.common.ui.components.FitScanNavBar
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.shape.CircleShape

@Composable
fun WorkoutListScreen(
    viewModel: WorkoutListViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val navController = rememberNavController() // Solo para preview, en la app real pásalo como prop

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF181A20))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 56.dp) // espacio para la nav bar
        ) {
            // Cabecera (TopBar)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Entrenamientos",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Row {
                    IconButton(onClick = { /* TODO: Buscar */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color.White)
                    }
                    IconButton(onClick = { /* TODO: Más opciones */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Más", tint = Color.White)
                    }
                }
            }

            // Contenido scrollable
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when {
                    uiState.isLoading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = dashboardGreen)
                        }
                    }
                    uiState.error != null -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = uiState.error ?: "Error", color = Color.White)
                                Button(onClick = { viewModel.retry() }) {
                                    Text("Reintentar")
                                }
                            }
                        }
                    }
                    else -> {
                        WorkoutSection(
                            title = "Tus entrenamientos",
                            showAddButton = true,
                            workouts = uiState.myWorkouts,
                            titleStyle = MaterialTheme.typography.titleLarge
                        )
                        WorkoutSection(
                            title = "Entrenamientos Populares 🔥",
                            showAddButton = false,
                            workouts = uiState.popularWorkouts,
                            titleStyle = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
        }
        // Barra de navegación inferior, pegada abajo
        FitScanNavBar(navController = navController)
    }
}

data class WorkoutUi(
    val name: String,
    val time: String,
    val level: String,
    val exercises: String
)

@Composable
fun WorkoutSection(
    title: String,
    showAddButton: Boolean,
    workouts: List<WorkoutUi>,
    titleStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.titleMedium
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = titleStyle,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        if (showAddButton) {
            IconButton(
                onClick = { /* TODO: Agregar entrenamiento */ },
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        color = dashboardGreen,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Agregar",
                    tint = Color.White
                )
            }
        }
    }
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        workouts.forEach {
            WorkoutCard(it)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun WorkoutCard(workout: WorkoutUi) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(dashboardCardBackground)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.FitnessCenter,
            contentDescription = null,
            tint = dashboardGreen,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = workout.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = workout.time,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = workout.level,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(dashboardGreen)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = workout.exercises,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutListScreenPreview() {
    // ViewModel simulado
    val fakeState = WorkoutListUiState(
        isLoading = false,
        myWorkouts = listOf(
            WorkoutUi("Fuerza de cuerpo completo", "45 min", "Avanzado", "8 ejercicios"),
            WorkoutUi("Carrera matutina", "45 min", "Avanzado", "8 ejercicios"),
            WorkoutUi("Tren superior", "45 min", "Avanzado", "8 ejercicios")
        ),
        popularWorkouts = listOf(
            WorkoutUi("Sesión HIIT", "45 min", "Avanzado", "8 ejercicios"),
            WorkoutUi("Carrera matutina", "45 min", "Avanzado", "8 ejercicios"),
            WorkoutUi("Tren superior", "45 min", "Avanzado", "8 ejercicios")
        )
    )
    val navController = rememberNavController()
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF181A20))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 56.dp)
            ) {
                // Cabecera
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Entrenamientos",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Row {
                        IconButton(onClick = { }) {
                            Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color.White)
                        }
                        IconButton(onClick = { }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Más", tint = Color.White)
                        }
                    }
                }
                WorkoutSection(
                    title = "Tus entrenamientos",
                    showAddButton = true,
                    workouts = fakeState.myWorkouts,
                    titleStyle = MaterialTheme.typography.titleLarge
                )
                WorkoutSection(
                    title = "Entrenamientos Populares 🔥",
                    showAddButton = false,
                    workouts = fakeState.popularWorkouts,
                    titleStyle = MaterialTheme.typography.titleLarge
                )
            }
            //FitScanNavBar(navController = navController)
        }
    }
}