package icesi.edu.co.fitscan.features.workout.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import icesi.edu.co.fitscan.features.workout.ui.viewmodel.ExerciseDetailState
import icesi.edu.co.fitscan.features.workout.ui.viewmodel.ExerciseDetailViewModel
import icesi.edu.co.fitscan.ui.theme.FitScanTheme
import androidx.compose.ui.graphics.Brush
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDetailScreen(
    workoutExerciseId: String? = null,
    workoutId: String? = null,
    onNavigateBack: () -> Unit = {},
    viewModel: ExerciseDetailViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(workoutExerciseId, workoutId) {
        if (!workoutExerciseId.isNullOrBlank() && !workoutId.isNullOrBlank()) {
            // Corregido: el id que se pasa debe ser el id de WorkoutExercise, y el workoutId el de la rutina
            viewModel.loadExerciseDetail(UUID.fromString(workoutId), UUID.fromString(workoutExerciseId))        }
    }
    
    val screenBackgroundColor = MaterialTheme.colorScheme.background
    val primaryTextColor = MaterialTheme.colorScheme.onBackground
    val secondaryTextColor = MaterialTheme.colorScheme.onSurfaceVariant
    val accentColor = MaterialTheme.colorScheme.primary
    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.tertiary)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del ejercicio", color = primaryTextColor) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = primaryTextColor
                        )
                    }
                },                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = screenBackgroundColor,
                    titleContentColor = primaryTextColor,
                    navigationIconContentColor = primaryTextColor
                )
            )
        },
        containerColor = screenBackgroundColor
    ) { innerPadding ->
        when (state) {
            is ExerciseDetailState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = accentColor)
                }
            }            is ExerciseDetailState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text((state as ExerciseDetailState.Error).message, color = MaterialTheme.colorScheme.error)
                }
            }
            is ExerciseDetailState.Success -> {
                val exercise = (state as ExerciseDetailState.Success).exercise
                // Defensive: fallback to empty string if null or blank (should never be null, but backend bug)
                val exerciseName = exercise.name ?: ""
                val exerciseDescription = exercise.description ?: ""
                val musclesWorkedRaw = exercise.muscleGroups ?: ""
                val musclesWorked = if (!musclesWorkedRaw.isNullOrBlank()) {
                    musclesWorkedRaw.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                } else emptyList()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                        .background(screenBackgroundColor)
                ) {
                    // Gradiente horizontal en vez de imagen, mientras vemos lo de las imagenes
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .background(gradientBrush, shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                    ) {}
                    // Sección Músculos trabajados
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Text(
                            text = "Músculos trabajados",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = primaryTextColor,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            musclesWorked.take(2).forEach { muscle ->
                                MuscleChip(text = muscle, isPrimary = muscle == musclesWorked.firstOrNull(), chipAccentColor = accentColor)
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            musclesWorked.drop(2).forEach { muscle ->
                                MuscleChip(text = muscle, chipAccentColor = accentColor)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    // Nombre, descripción, pasos
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Text(
                            text = exerciseName,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = accentColor,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = exerciseDescription,
                            style = MaterialTheme.typography.bodyLarge,
                            color = secondaryTextColor,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MuscleChip(text: String, isPrimary: Boolean = false, chipAccentColor: Color) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, chipAccentColor),
        color = Color.Transparent,
        contentColor = chipAccentColor
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            if (isPrimary) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Músculo primario",
                    tint = chipAccentColor,
                    modifier = Modifier.size(16.dp).padding(end = 4.dp)
                )
            }
            Text(
                text = text,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExerciseDetailScreenPreview() {
    FitScanTheme {
        ExerciseDetailScreen()
    }
}