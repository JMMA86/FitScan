package icesi.edu.co.fitscan.features.statistics.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.background
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Brush
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.foundation.shape.CircleShape
import icesi.edu.co.fitscan.features.statistics.ui.viewmodel.ExerciseProgressViewModel
import icesi.edu.co.fitscan.features.statistics.ui.viewmodel.TimeRange
import icesi.edu.co.fitscan.ui.theme.greenLess
import icesi.edu.co.fitscan.ui.theme.greyStrong
import icesi.edu.co.fitscan.ui.theme.greyMed
import icesi.edu.co.fitscan.ui.theme.redDangerous
import icesi.edu.co.fitscan.features.common.ui.components.FitScanTextField
import icesi.edu.co.fitscan.features.common.ui.components.ExerciseSearchCard
import icesi.edu.co.fitscan.features.common.ui.components.FitScanLineChart

@Composable
fun ExerciseProgressScreen(
    viewModel: ExerciseProgressViewModel = viewModel()
) {
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
                    val year = parts[0].toInt()
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(greyStrong)
            .padding(16.dp)
    ) {
        Text(
            text = "Progreso por ejercicio",
            style = MaterialTheme.typography.headlineSmall,
            color = greenLess
        )
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
                        .background(greyMed, shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cancelar búsqueda",
                        tint = Color.White
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
                    .heightIn(max = 300.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(greyMed, greyStrong)
                        ),
                        shape = RoundedCornerShape(10.dp),
                    )
            ) {
                LazyColumn (modifier=Modifier.background(greyStrong)) {
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
                TimeRange.values().forEach { range ->
                    val selected = timeRange == range
                    Button(
                        onClick = { viewModel.setTimeRange(range) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selected) greenLess else greyMed,
                            contentColor = Color.White
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
                ) {
                    if (!selectedExercise.isNullOrBlank()) {
                        Text(
                            text = selectedExercise ?: "",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    when {
                        isLoading -> {
                            CircularProgressIndicator(color = greenLess)
                        }
                        error != null -> {
                            Text(
                                text = error ?: "Error",
                                color = redDangerous,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        chartData.isNotEmpty() -> {
                            FitScanLineChart(data = chartData, labels = formattedDateLabels)
                        }
                        else -> {
                            Text(
                                text = "No hay datos para mostrar",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ExerciseProgressScreenPreview() {
    ExerciseProgressScreen()
}
