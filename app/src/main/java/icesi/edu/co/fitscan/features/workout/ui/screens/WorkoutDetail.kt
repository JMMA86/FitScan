package icesi.edu.co.fitscan.features.workout.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import icesi.edu.co.fitscan.R
import icesi.edu.co.fitscan.features.workout.ui.viewmodel.WorkoutDetailState
import icesi.edu.co.fitscan.features.workout.ui.viewmodel.WorkoutDetailViewModel
import icesi.edu.co.fitscan.features.workout.ui.viewmodel.factory.WorkoutDetailViewModelFactory
import icesi.edu.co.fitscan.ui.theme.FitScanTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutDetailScreen(
    workoutId: String? = null, // Ahora es nullable
    onNavigateBack: () -> Unit = {},
    onStartWorkout: () -> Unit = {
    },
    onEditWorkout: () -> Unit = {},
    onExerciseClick: (String, String) -> Unit = { _, _ -> } // Ahora recibe workoutId y workoutExerciseId
) {
    val viewModel: WorkoutDetailViewModel = viewModel(factory = WorkoutDetailViewModelFactory())
    val state by viewModel.state.collectAsState()

    // Cargar la rutina al entrar
    LaunchedEffect(workoutId) {
        if (workoutId.isNullOrBlank()) {
            viewModel.loadFirstWorkout()
        } else {
            viewModel.loadWorkout(workoutId)
        }
    }    // Colores del tema
    val screenBackgroundColor = MaterialTheme.colorScheme.background
    val primaryTextColor = MaterialTheme.colorScheme.onBackground
    val secondaryTextColor = MaterialTheme.colorScheme.onSurfaceVariant
    val accentColor = MaterialTheme.colorScheme.primary
    val cardBackgroundColor = MaterialTheme.colorScheme.surfaceVariant // For exercise item cards

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de entrenamiento", color = primaryTextColor) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = primaryTextColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = screenBackgroundColor,
                    titleContentColor = primaryTextColor,
                    navigationIconContentColor = primaryTextColor
                )
            )
        },
        containerColor = screenBackgroundColor
    ) { innerPadding ->
        when (state) {
            is WorkoutDetailState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = accentColor)
                }
            }

            is WorkoutDetailState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text((state as WorkoutDetailState.Error).message, color = MaterialTheme.colorScheme.error)
                }
            }

            is WorkoutDetailState.Success -> {
                val workout = (state as WorkoutDetailState.Success).workout
                val exercises =
                    (state as WorkoutDetailState.Success).exercises // Now UiWorkoutExercise
                val workoutName = workout.name
                val workoutDuration = "${workout.durationMinutes ?: "-"} min"
                val workoutDifficulty = workout.difficulty ?: "-"
                val workoutTags = listOfNotNull(workout.type, "${exercises.size} ejercicios")
                // Ahora exercises es la lista real de la rutina

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                        .background(screenBackgroundColor)
                        .padding(horizontal = 16.dp) // General horizontal padding for content
                ) {
                    // Workout Title and Edit Button
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 8.dp),
                        verticalAlignment = Alignment.Top, // Align edit button to the top of the title block
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = workoutName,
                            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                            color = primaryTextColor,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp) // Give title space
                        )
                        IconButton(
                            onClick = onEditWorkout,
                            modifier = Modifier
                                .size(40.dp) // Standard FAB mini size
                                .clip(CircleShape)
                                .background(accentColor.copy(alpha = 0.3f)) // Lighter accent for background
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Edit, // Este ícono sí estaba bien
                                contentDescription = "Editar entrenamiento",
                                tint = accentColor // Icon itself is the accent color
                            )
                        }
                    }

                    // Workout Info (Duration, Difficulty)
                    Row(
                        modifier = Modifier.padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // TODO : Use actual icons for duration and difficulty
                        InfoItem(
                            icon = Icons.Filled.Edit,
                            text = workoutDuration,
                            color = secondaryTextColor
                        )
                        InfoItem(
                            icon = Icons.Filled.Build,
                            text = workoutDifficulty,
                            color = secondaryTextColor
                        )
                    }

                    // Workout Tags
                    Row(
                        modifier = Modifier.padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        workoutTags.forEach { tag ->
                            TagChip(
                                text = tag.toString(),
                                backgroundColor = cardBackgroundColor,
                                textColor = accentColor
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Exercise List
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        exercises.forEach { item ->
                            ExerciseRow(
                                name = item.name, // Show the name, not the ID
                                sets = item.sets,
                                reps = item.reps,
                                onClick = { onExerciseClick(workoutId ?: "", item.id) },
                                cardBackgroundColor = cardBackgroundColor,
                                primaryTextColor = primaryTextColor,
                                secondaryTextColor = secondaryTextColor,
                                accentColor = accentColor
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp)) // Space before the button

                    // Start Workout Button
                    Button(
                        onClick = onStartWorkout,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_fitness), // Your dumbbell icon
                            contentDescription = null, // Decorative
                            tint = primaryTextColor, // Icon color on button
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Empezar entrenamiento",
                            color = primaryTextColor, // Text color on button
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp)) // Padding at the very bottom
                }
            }
        }
    }
}

@Composable
private fun InfoItem(icon: ImageVector, text: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = text, color = color, fontSize = 14.sp)
    }
}

@Composable
private fun TagChip(text: String, backgroundColor: Color, textColor: Color) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        modifier = Modifier
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun ExerciseRow(
    name: String,
    sets: Int,
    reps: Int,
    onClick: () -> Unit,
    cardBackgroundColor: Color,
    primaryTextColor: Color = MaterialTheme.colorScheme.onSurface,
    secondaryTextColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    accentColor: Color = MaterialTheme.colorScheme.primary
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = cardBackgroundColor
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    color = primaryTextColor,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$sets sets • $reps repeticiones",
                    style = MaterialTheme.typography.bodySmall,
                    color = secondaryTextColor
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Ver detalle",
                tint = accentColor,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutDetailScreenPreview() {
    FitScanTheme {
        WorkoutDetailScreen()
    }
}