package icesi.edu.co.fitscan.features.statistics.ui.screens

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import icesi.edu.co.fitscan.features.statistics.ui.viewmodel.ExerciseProgressViewModel
import icesi.edu.co.fitscan.features.common.ui.components.FitScanHeader
import icesi.edu.co.fitscan.features.common.ui.components.FitScanLineChart
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import icesi.edu.co.fitscan.features.common.ui.components.ExerciseSearchCard
import icesi.edu.co.fitscan.features.statistics.ui.viewmodel.TimeRange
import icesi.edu.co.fitscan.features.statistics.ui.viewmodel.factory.ExerciseProgressViewModelFactory
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Brush
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.tooling.preview.Preview
import icesi.edu.co.fitscan.features.common.ui.components.FitScanTextField
import icesi.edu.co.fitscan.ui.theme.FitScanTheme
@Composable
fun ExerciseProgressScreen(
    navController: NavController = rememberNavController()
) {
    val viewModel: ExerciseProgressViewModel = viewModel(factory = ExerciseProgressViewModelFactory())
    val selectedExercise by viewModel.selectedExercise.collectAsState()
    val timeRange by viewModel.timeRange.collectAsState()
    val chartData by viewModel.chartData.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val availableExercises by viewModel.availableExercises.collectAsState()
    val dateLabelsRaw by viewModel.dateLabels.collectAsState()

    // Format date labels (e.g., may-16, abr-05)
    val formattedDateLabels = remember(dateLabelsRaw) {
        dateLabelsRaw.map { dateStr ->
            try {
                val parts = dateStr.split("T")[0].split("-")
                if (parts.size == 3) {
                    val month = parts[1].toInt()
                    val day = parts[2].toInt()
                    val monthShort = when (month) {
                        1 -> "ene"; 2 -> "feb"; 3 -> "mar"; 4 -> "abr"; 5 -> "may"; 6 -> "jun"
                        7 -> "jul"; 8 -> "ago"; 9 -> "sep"; 10 -> "oct"; 11 -> "nov"; 12 -> "dic"
                        else -> ""
                    }
                    "$monthShort-${day.toString().padStart(2, '0')}"
                } else dateStr
            } catch (e: Exception) { dateStr }
        }
    }

    Column {
        FitScanHeader(
            title = "Progreso por ejercicio",            navController = navController
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            var isSearching by remember { mutableStateOf(false) }
            var query by remember { mutableStateOf(selectedExercise ?: "") }
            val filteredExercises = remember(query to availableExercises) {
                availableExercises.filter { it.name.contains(query, ignoreCase = true) }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.weight(1f)) {
                    FitScanTextField(
                        value = query,
                        onValueChange = {
                            query = it
                            isSearching = true
                        },
                        placeholder = "Ejercicio",
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { focusState ->
                                isSearching = focusState.isFocused
                            }
                    )
                }
                if (isSearching) {
                    Spacer(modifier= Modifier.width(20.dp))
                    
                    IconButton(
                        onClick = {
                            isSearching = false
                            query = selectedExercise ?: ""
                        },
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .size(36.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant, shape = CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cancelar búsqueda",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            if (isSearching) {
                Surface(
                    tonalElevation = 8.dp,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .heightIn(max = 300.dp)                        .background(
                            brush = Brush.verticalGradient(
                                listOf(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.surface)
                            ),
                            shape = RoundedCornerShape(10.dp),
                        )
                ) {
                    LazyColumn (modifier=Modifier.background(MaterialTheme.colorScheme.surface)) {
                        items(filteredExercises) { exercise ->
                            ExerciseSearchCard(
                                name = exercise.name,
                                onClick = {
                                    query = exercise.name
                                    viewModel.setExercise(exercise.name)
                                    isSearching = false
                                }
                            )
                        }
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TimeRange.entries.forEach { range ->
                        val selected = timeRange == range
                        
                        Button(
                            onClick = { viewModel.setTimeRange(range) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                                contentColor = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier.weight(1f).padding(horizontal = 1.dp)
                        ) {
                            Text(
                                text = when (range) {
                                    TimeRange.LAST_WEEK -> "7d"
                                    TimeRange.LAST_MONTH -> "1m"
                                    TimeRange.LAST_3_MONTHS -> "3m"
                                    TimeRange.LAST_6_MONTHS -> "6m"
                                    TimeRange.LAST_12_MONTHS -> "12m"
                                },
                                fontSize = 12.sp
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {                        if (!selectedExercise.isNullOrBlank()) {
                            Text(
                                text = selectedExercise ?: "",
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                        when {
                            isLoading -> {
                                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                            }
                            error != null -> {
                                Text(
                                    text = error ?: "Error",
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            chartData.isNotEmpty() -> {
                                FitScanLineChart(data = chartData, labels = formattedDateLabels)
                            }
                            else -> {
                                Text(
                                    text = "No hay datos para mostrar",
                                    color = MaterialTheme.colorScheme.onBackground,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun ExerciseProgressScreenPreview1() {
    ExerciseProgressScreen(navController = rememberNavController())
}