package icesi.edu.co.fitscan.features.statistics.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import icesi.edu.co.fitscan.domain.model.MuscleGroup
import icesi.edu.co.fitscan.domain.model.MuscleGroupProgress
import icesi.edu.co.fitscan.features.common.ui.components.FitScanHeader
import icesi.edu.co.fitscan.features.statistics.ui.components.PolygonChart
import icesi.edu.co.fitscan.features.statistics.ui.viewmodel.MuscleGroupProgressViewModel
import icesi.edu.co.fitscan.features.statistics.ui.viewmodel.MuscleGroupProgressUiState
import icesi.edu.co.fitscan.features.statistics.ui.viewmodel.factory.MuscleGroupProgressViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MuscleGroupProgressScreen(
    navController: NavController,
    viewModel: MuscleGroupProgressViewModel = viewModel(factory = MuscleGroupProgressViewModelFactory())
) {    val uiState by viewModel.uiState.collectAsState()
    val selectedMuscleGroup by viewModel.selectedMuscleGroup.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Progreso por Grupo Muscular",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                },                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.retry() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Actualizar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (uiState) {
            is MuscleGroupProgressUiState.Loading -> {
                LoadingState()
            }
            is MuscleGroupProgressUiState.Success -> {
                val successState = uiState as MuscleGroupProgressUiState.Success
                SuccessContent(
                    muscleGroups = successState.muscleGroups,
                    progressData = successState.progressData,
                    selectedMuscleGroup = selectedMuscleGroup,
                    onMuscleGroupSelected = viewModel::selectMuscleGroup,
                    onClearSelection = viewModel::clearSelection
                )
            }            is MuscleGroupProgressUiState.Error -> {
                val errorState = uiState as MuscleGroupProgressUiState.Error
                ErrorState(
                    message = errorState.message,
                    onRetry = viewModel::retry
                )
            }
        }
        }
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Cargando progreso...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Error al cargar datos",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text("Reintentar")
            }
        }
    }
}

@Composable
private fun SuccessContent(
    muscleGroups: List<MuscleGroup>,
    progressData: List<MuscleGroupProgress>,
    selectedMuscleGroup: MuscleGroup?,
    onMuscleGroupSelected: (MuscleGroup) -> Unit,
    onClearSelection: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        PolygonChartCard(progressData = progressData)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Grupos Musculares",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Scrollable list only for progressData
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), // Take remaining space and allow scrolling
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(progressData) { progress ->
                MuscleGroupProgressCard(
                    progress = progress,
                    isSelected = selectedMuscleGroup?.id == progress.muscleGroup.id,
                    onClick = {
                        if (selectedMuscleGroup?.id == progress.muscleGroup.id) {
                            onClearSelection()
                        } else {
                            onMuscleGroupSelected(progress.muscleGroup)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun PolygonChartCard(progressData: List<MuscleGroupProgress>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Vista General del Progreso",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            PolygonChart(
                muscleGroupProgress = progressData,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
            
            // Legend
            Text(
                text = "Cada punto representa el progreso de un grupo muscular",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun OverallStatsCard(progressData: List<MuscleGroupProgress>) {
    val averageProgress = if (progressData.isNotEmpty()) {
        progressData.map { it.progressPercentage }.average().toFloat()
    } else 0f
    
    val totalExercises = progressData.sumOf { it.totalExercises }
    val totalCompletedThisWeek = progressData.sumOf { it.completedThisWeek }
    
    // Find muscle group with maximum sets
    val topMuscleGroup = progressData.maxByOrNull { it.completedThisWeek }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Estadísticas Generales",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    title = "Progreso Promedio",
                    value = "${(averageProgress * 100).toInt()}%",
                    color = MaterialTheme.colorScheme.primary
                )
                  StatItem(
                    title = "Ejercicios Totales",
                    value = totalExercises.toString(),
                    color = MaterialTheme.colorScheme.secondary
                )
                
                // Show muscle group with most sets instead of total sets this week
                if (topMuscleGroup != null) {
                    StatItem(
                        title = topMuscleGroup.muscleGroup.name,
                        value = "${topMuscleGroup.completedThisWeek} sets",
                        color = MaterialTheme.colorScheme.tertiary
                    )
                } else {
                    StatItem(
                        title = "Sin datos",
                        value = "0 sets",
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }
    }
}

@Composable
private fun StatItem(
    title: String,
    value: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(100.dp) // Add fixed width for better layout
    ) {        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            maxLines = 2 // Allow muscle group names to wrap to 2 lines
        )
    }
}

@Composable
private fun MuscleGroupProgressCard(
    progress: MuscleGroupProgress,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = progress.muscleGroup.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    text = "${(progress.progressPercentage * 100).toInt()}%",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LinearProgressIndicator(
                progress = { progress.progressPercentage },
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Ejercicios: ${progress.totalExercises}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Text(
                    text = "Esta semana: ${progress.completedThisWeek}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            if (progress.totalVolume > 0) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Volumen total: ${progress.totalVolume.toInt()} kg",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
