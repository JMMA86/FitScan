package icesi.edu.co.fitscan.features.workout.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import icesi.edu.co.fitscan.R
import icesi.edu.co.fitscan.features.workout.ui.model.PerformWorkoutUiState
import icesi.edu.co.fitscan.features.workout.ui.viewmodel.PerformWorkoutViewModel
import icesi.edu.co.fitscan.features.workout.ui.viewmodel.factory.PerformWorkoutViewModelFactory
import icesi.edu.co.fitscan.features.workout.ui.components.ExerciseImageThumbnail
import icesi.edu.co.fitscan.ui.theme.Dimensions

@Composable
fun PerformWorkoutScreen(
    modifier: Modifier = Modifier,
    workoutId: String = "b48b68ba-1863-4ca7-87f7-5b32a5f4414e",
    onFinishWorkout: () -> Unit,
    onNavigateToExerciseDetail: (workoutId: String, workoutExerciseId: String) -> Unit = { _, _ -> }
) {
    // Usar viewModel con key para preservar la instancia específica del workout
    val viewModel: PerformWorkoutViewModel = viewModel(
        key = "workout_$workoutId",
        factory = PerformWorkoutViewModelFactory(workoutId)
    )

    // Solo inicializar el workout si no ha sido iniciado previamente
    LaunchedEffect(workoutId) {
        if (!viewModel.isWorkoutStarted()) {
            viewModel.startWorkout()
        }
    }

    val uiState by viewModel.uiState.collectAsState()
    var showFinishDialog = remember { mutableStateOf(false) }

    if (showFinishDialog.value) {
        AlertDialog(
            onDismissRequest = { showFinishDialog.value = false },
            title = { Text("¿Terminar entrenamiento?") },
            text = { Text("No se guardarán los ejercicios que falten por completar. ¿Deseas continuar?") },
            confirmButton = {
                TextButton(onClick = {
                    showFinishDialog.value = false
                    viewModel.finishWorkout()
                    onFinishWorkout()
                }) { Text("Sí") }
            },
            dismissButton = {
                TextButton(onClick = { showFinishDialog.value = false }) { Text("No") }
            }
        )
    }

    PerformWorkoutScreenContent(
        modifier = modifier,
        workoutId = workoutId,
        viewModel = viewModel,
        uiState = uiState,
        onEndSet = { viewModel.endSet() },
        onSkipToNextExercise = { viewModel.skipToNextExercise() },
        onNavigateToExerciseDetail = onNavigateToExerciseDetail,
        onFinishWorkout = {
            if (viewModel.hasUnfinishedExercises()) {
                showFinishDialog.value = true
            } else {
                viewModel.finishWorkout()
                onFinishWorkout()
            }
        }
    )
}

@Composable
fun PerformWorkoutListComponent(
    title: String = "NA",
    sets: String = "NA",
    reps: String = "NA",
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Dimensions.MediumCornerRadius))
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(Dimensions.MediumCornerRadius)
            )
            .clickable { onClick() }
            .padding(Dimensions.MediumPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Exercise thumbnail
        ExerciseImageThumbnail(
            exerciseName = title,
            size = 40,
            isCircular = true
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = Dimensions.LargeTextSize,
            )
            Text(
                text = "$sets sets ~ $reps reps",
                color = MaterialTheme.colorScheme.primary,
                fontSize = Dimensions.SmallTextSize,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onClick,
            shape = RectangleShape,
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .size(Dimensions.LargeIconSize)
                .clip(RoundedCornerShape(Dimensions.SmallCornerRadius))
        ) {
            Icon(
                painter = painterResource(id = R.drawable.chevron_right),
                contentDescription = "Mostrar ejercicio",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(Dimensions.LargeIconSize)
            )
        }
    }
}

// --- Main Content ---
@Composable
fun PerformWorkoutScreenContent(
    modifier: Modifier,
    workoutId: String,
    viewModel: PerformWorkoutViewModel,
    uiState: PerformWorkoutUiState,
    onEndSet: () -> Unit = {},
    onSkipToNextExercise: () -> Unit = {},
    onNavigateToExerciseDetail: (workoutId: String, workoutExerciseId: String) -> Unit = { _, _ -> },
    onFinishWorkout: () -> Unit = {}
) {
    when (uiState) {
        is PerformWorkoutUiState.Idle -> {
            // Optionally show nothing or a placeholder
        }

        is PerformWorkoutUiState.Loading -> {
            WorkoutLoadingScreen()
        }

        is PerformWorkoutUiState.Error -> {
            val message = (uiState as PerformWorkoutUiState.Error).message
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("Error: $message", color = Color.Red)
            }
        }

        is PerformWorkoutUiState.Success -> {
            val data = (uiState as PerformWorkoutUiState.Success).data

            LazyColumn(
                modifier.background(MaterialTheme.colorScheme.background)
            ) {
                item {
                    // Header, subtitles and progress
                    Spacer(modifier = Modifier.height(Dimensions.SmallPadding))
                    WorkoutHeader(
                        title = data.title,
                        subtitle = data.subtitle,
                        progress = data.progress
                    )
                    Spacer(modifier = Modifier.height(Dimensions.SmallPadding))

                    // Current exercise
                    CurrentExerciseSection(
                        name = data.currentExercise.name,
                        time = data.currentExercise.time,
                        series = data.currentExercise.series,
                        remainingTime = data.currentExercise.remainingTime,
                        repetitions = data.currentExercise.repetitions,
                        initialRepsValues = data.currentExercise.repsValues,
                        initialKilosValues = data.currentExercise.kilosValues,
                        onRepsChanged = { viewModel.updateRepsValues(it) },
                        onKilosChanged = { viewModel.updateKilosValues(it) },
                        onSetsCountChanged = { viewModel.updateSetsCount(it) },
                        isTimeExceeded = data.currentExercise.isTimeExceeded,
                        onExerciseDetailClick = {
                            if (data.currentExercise.workoutExerciseId.isNotEmpty()) {
                                onNavigateToExerciseDetail(workoutId, data.currentExercise.workoutExerciseId)
                            }
                        }
                    )

                    // Next exercise
                    NextExerciseSection(
                        name = data.nextExercise.name,
                        sets = data.nextExercise.sets.toString(),
                        reps = data.nextExercise.reps.toString(),
                        onClick = {
                            if (data.nextExercise.workoutExerciseId.isNotEmpty()) {
                                onNavigateToExerciseDetail(workoutId, data.nextExercise.workoutExerciseId)
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(Dimensions.smallestPadding))

                    // Controls
                    WorkoutControls(
                        onPrevious = { viewModel.goToPreviousExercise() },
                        onPause = { viewModel.pauseExercise() },
                        onNext = { viewModel.goToNextExercise() }
                    )
                    Spacer(modifier = Modifier.height(Dimensions.smallestPadding))
                }

                // Remaining exercises list
                items(data.remainingExercises.size) { index ->
                    val exercise = data.remainingExercises[index]
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Dimensions.MediumPadding),
                    ) {
                        PerformWorkoutListComponent(
                            title = exercise.title,
                            sets = exercise.sets,
                            reps = exercise.reps,
                            onClick = {
                                if (exercise.workoutExerciseId.isNotEmpty()) {
                                    onNavigateToExerciseDetail(workoutId, exercise.workoutExerciseId)
                                }
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(Dimensions.SmallPadding))
                }

                // Finish workout button
                item {
                    FinishWorkoutButton(onFinish = onFinishWorkout)
                }
            }
        }
    }
}

@Preview
@Composable
fun PerformWorkoutScreenPreview() {

}

@Composable
fun WorkoutLoadingScreen() {
    val infiniteTransition = rememberInfiniteTransition(label = "WorkoutLoading")

    // Animación de escala para el ícono principal
    val iconScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "IconScale"
    )

    // Animación de opacidad para los elementos flotantes
    val floatingAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "FloatingAlpha"
    )

    // Animación de rotación para elementos de fondo
    val backgroundRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000),
            repeatMode = RepeatMode.Restart
        ),
        label = "BackgroundRotation"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f)
                    ),
                    radius = 800f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Elementos flotantes decorativos con animación
            Box(
                modifier = Modifier.size(220.dp),
                contentAlignment = Alignment.Center
            ) {
                // Círculos concéntricos animados
                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .alpha(floatingAlpha * 0.2f)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                    Color.Transparent
                                )
                            ),
                            CircleShape
                        )
                )

                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .alpha(floatingAlpha * 0.4f)
                        .scale(iconScale * 0.8f)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.tertiary.copy(alpha = 0.4f),
                                    Color.Transparent
                                )
                            ),
                            CircleShape
                        )
                )

                // Ícono principal con múltiples animaciones
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.FitnessCenter,
                        contentDescription = "Loading Workout",
                        modifier = Modifier
                            .size(40.dp)
                            .scale(iconScale),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Progress indicator mejorado
            Box(contentAlignment = Alignment.Center) {
                // Indicador animado
                CircularProgressIndicator(
                    modifier = Modifier.size(50.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 4.dp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Texto principal mejorado
            Text(
                text = "🏋️ Preparando tu entrenamiento",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Texto secundario con animación mejorada
            Text(
                text = "Configurando ejercicios personalizados...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .alpha(floatingAlpha)
            )

            Spacer(modifier = Modifier.height(60.dp))

            // Indicadores de progreso mejorados como barras
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                repeat(4) { index ->
                    val barHeight by infiniteTransition.animateFloat(
                        initialValue = 4.dp.value,
                        targetValue = 16.dp.value,
                        animationSpec = infiniteRepeatable(
                            animation = tween(800),
                            repeatMode = RepeatMode.Reverse,
                            initialStartOffset = StartOffset(index * 150)
                        ),
                        label = "BarHeight$index"
                    )

                    Box(
                        modifier = Modifier
                            .size(width = 8.dp, height = barHeight.dp)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.secondary
                                    )
                                ),
                                RoundedCornerShape(4.dp)
                            )
                    )
                }
            }
        }

        // Elementos decorativos flotantes mejorados
        FloatingElement(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(20.dp),
            size = 60.dp,
            alpha = floatingAlpha * 0.4f,
            color = MaterialTheme.colorScheme.tertiary
        )

        FloatingElement(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(40.dp),
            size = 40.dp,
            alpha = floatingAlpha * 0.6f,
            color = MaterialTheme.colorScheme.secondary
        )

        FloatingElement(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(60.dp),
            size = 50.dp,
            alpha = floatingAlpha * 0.3f,
            color = MaterialTheme.colorScheme.primary
        )

        FloatingElement(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(30.dp),
            size = 35.dp,
            alpha = floatingAlpha * 0.5f,
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}

@Composable
private fun FloatingElement(
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp,
    alpha: Float,
    color: Color
) {
    Box(
        modifier = modifier
            .size(size)
            .alpha(alpha)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        color.copy(alpha = 0.3f),
                        Color.Transparent
                    )
                ),
                CircleShape
            )
    )
}

@Preview(showBackground = true)
@Composable
fun WorkoutLoadingScreenPreview() {
    MaterialTheme {
        WorkoutLoadingScreen()
    }
}