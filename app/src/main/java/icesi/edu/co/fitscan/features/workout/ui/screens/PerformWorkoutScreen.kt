package icesi.edu.co.fitscan.features.workout.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import icesi.edu.co.fitscan.R
import icesi.edu.co.fitscan.features.workout.ui.model.PerformWorkoutUiState
import icesi.edu.co.fitscan.features.workout.ui.viewmodel.PerformWorkoutViewModel
import icesi.edu.co.fitscan.features.workout.ui.viewmodel.factory.PerformWorkoutViewModelFactory
import icesi.edu.co.fitscan.ui.theme.Dimensions

@Composable
fun PerformWorkoutScreen(
    modifier: Modifier = Modifier,
    workoutId: String = "b48b68ba-1863-4ca7-87f7-5b32a5f4414e",
    onFinishWorkout: () -> Unit
) {
    val viewModel: PerformWorkoutViewModel = viewModel(
        factory = PerformWorkoutViewModelFactory(workoutId)
    )

    // Call startWorkout only once when the screen is shown
    LaunchedEffect(workoutId) {
        viewModel.startWorkout()
    }

    val uiState by viewModel.uiState.collectAsState()

    PerformWorkoutScreenContent(
        modifier = modifier,
        viewModel = viewModel,
        uiState = uiState,
        onEndSet = { viewModel.endSet() },
        onSkipToNextExercise = { viewModel.skipToNextExercise() },
        onFinishWorkout = {
            viewModel.finishWorkout()
            onFinishWorkout()
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
            .padding(Dimensions.MediumPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
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
    viewModel: PerformWorkoutViewModel,
    uiState: PerformWorkoutUiState,
    onEndSet: () -> Unit = {},
    onSkipToNextExercise: () -> Unit = {},
    onFinishWorkout: () -> Unit = {}
) {
    when (uiState) {
        is PerformWorkoutUiState.Idle -> {
            // Optionally show nothing or a placeholder
        }

        is PerformWorkoutUiState.Loading -> {
            // Show a loading indicator
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("Loading...")
            }
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
                        remainingTime = data.currentExercise.remainingTime
                    )

                    // Next exercise
                    NextExerciseSection(
                        name = data.nextExercise.name,
                        sets = data.nextExercise.sets.toString(),
                        reps = data.nextExercise.reps.toString()
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
                            onClick = { /* TODO: Show exercise details or navigate */ }
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