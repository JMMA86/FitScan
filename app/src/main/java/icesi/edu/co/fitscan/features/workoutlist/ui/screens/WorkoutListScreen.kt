package icesi.edu.co.fitscan.features.workoutlist.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import icesi.edu.co.fitscan.features.common.ui.components.RecentActivityCard
import icesi.edu.co.fitscan.features.workoutlist.ui.model.WorkoutListUiState
import icesi.edu.co.fitscan.features.workoutlist.ui.viewmodel.WorkoutListViewModel
import icesi.edu.co.fitscan.features.workoutlist.ui.viewmodel.factory.WorkoutListViewModelFactory
import icesi.edu.co.fitscan.ui.theme.greenLess
import icesi.edu.co.fitscan.ui.theme.greyStrong

@Composable
fun WorkoutListScreen(
    onNavigateToCreate: () -> Unit,
    onNavigateToPerform: (workoutId: String) -> Unit,
    viewModel: WorkoutListViewModel = viewModel(factory = WorkoutListViewModelFactory())
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(searchQuery) {
        viewModel.onSearchQueryChanged(searchQuery)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(greyStrong)
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Entrenamientos",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(
                onClick = onNavigateToCreate,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = greenLess
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Crear rutina",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Buscar rutinas...", color = Color.Gray) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = Color.Gray
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.Gray,
                focusedPlaceholderColor = Color.Gray,
                unfocusedPlaceholderColor = Color.Gray,
                cursorColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Workout list usando RecentActivityCard
        when (uiState) {
            is WorkoutListUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
            is WorkoutListUiState.Success -> {
                val workouts = (uiState as WorkoutListUiState.Success).workouts
                if (workouts.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay rutinas disponibles",
                            color = Color.Gray
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(workouts) { workout ->
                            RecentActivityCard(
                                title = workout.name,
                                time = workout.durationMinutes?.let { "$it min" } ?: "Sin tiempo",
                                level = workout.difficulty ?: "Sin nivel",
                                exercises = workout.type.name,
                            )
                            // Navegación al tocar la tarjeta
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(Color.Transparent)
                                    .clickable { onNavigateToPerform(workout.id.toString()) }
                            )
                        }
                    }
                }
            }
            is WorkoutListUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (uiState as WorkoutListUiState.Error).message,
                        color = Color.Red
                    )
                }
            }
            else -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay entrenamientos disponibles",
                        color = Color.Gray
                    )
                }
            }
        }
    }
}