package icesi.edu.co.fitscan.features.home.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import icesi.edu.co.fitscan.R
import icesi.edu.co.fitscan.features.common.ui.components.OptionButton
import icesi.edu.co.fitscan.features.common.ui.viewmodel.AppState
import icesi.edu.co.fitscan.features.home.ui.viewmodel.DashboardViewModel
import icesi.edu.co.fitscan.features.home.ui.viewmodel.factory.DashboardViewModelFactory
import icesi.edu.co.fitscan.features.home.ui.components.CircularProgress
import icesi.edu.co.fitscan.features.home.ui.components.MetricContainer
import icesi.edu.co.fitscan.features.common.ui.components.RecentActivityCard
import icesi.edu.co.fitscan.navigation.Screen
import icesi.edu.co.fitscan.ui.theme.FitScanTheme

@Composable
fun DashboardScreen(
    navController: NavController
) {
    val viewModel: DashboardViewModel = viewModel(factory = DashboardViewModelFactory())
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val weeklyMetrics by viewModel.weeklyMetrics.collectAsState()
    val weeklyProgress by viewModel.weeklyProgress.collectAsState()
    val recentActivities by viewModel.recentActivities.collectAsState()

    LaunchedEffect(Unit) {
        AppState.customerId?.let { userId ->
            viewModel.loadDashboardData(userId)
        }
    }

    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize()) {        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // CABECERA CON ICONOS
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {                IconButton(onClick = { navController.navigate("profile") }) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Usuario",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Configuración",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                contentAlignment = Alignment.Center
            ) {                Text(
                    text = "FitScanAI",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else if (error != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {                        Text(
                            text = error ?: "Error desconocido",
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { AppState.customerId?.let { viewModel.retryLoading(it) } }) {
                            Text("Reintentar")
                        }
                    }
                }
            } else {
                weeklyMetrics?.let { metrics ->
                    MetricContainer(
                        Triple(metrics.trainingHours, metrics.trainedDays, metrics.totalDistance)
                    )                }

                Spacer(modifier = Modifier.height(20.dp))
                
                Text(
                    text = "Progreso de esta semana",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 22.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgress(progress = weeklyProgress)
                }

                if (recentActivities.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {                        Text(
                            text = "Actividades recientes",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 22.sp,
                        )
                        IconButton(
                            onClick = { navController.navigate(Screen.Workouts.route) }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Ver rutinas",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .height(250.dp)
                            .padding(horizontal = 16.dp)
                    ) {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(2.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(recentActivities) { activity ->
                                RecentActivityCard(
                                    id = activity.id,
                                    title = activity.title,
                                    time = activity.time,
                                    level = activity.level,
                                    exercises = activity.exercises,
                                    onClick = { workoutId ->
                                        navController.navigate("workout_detail/$workoutId")
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }

                Text(
                    text = "Actividades",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 22.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                val options = listOf("Crear Entrenamiento", "Progreso Visual")
                val icons = listOf(R.drawable.ic_fitness, R.drawable.ic_statistics)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Botón: Crear Entrenamiento - Izquierda
                    OptionButton(
                        label = "Crear Entrenamiento",
                        selected = false,
                        onClick = { navController.navigate(Screen.CreateWorkout.route) },
                        modifier = Modifier.weight(1f),
                        options = options,
                        icons = icons
                    )

                    Spacer(modifier = Modifier.width(8.dp)) // Pequeño espacio opcional

                    // Botón: Progreso Visual - Derecha
                    OptionButton(
                        label = "Progreso Visual",
                        selected = false,
                        onClick = { navController.navigate(Screen.Statistics.route) },
                        modifier = Modifier.weight(1f),
                        options = options,
                        icons = icons
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    FitScanTheme {
        DashboardScreen(navController = rememberNavController())
    }
}