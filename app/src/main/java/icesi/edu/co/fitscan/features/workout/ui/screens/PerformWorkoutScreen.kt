package icesi.edu.co.fitscan.features.workout.ui.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import icesi.edu.co.fitscan.R
import icesi.edu.co.fitscan.features.common.data.remote.RetrofitInstance
import icesi.edu.co.fitscan.features.common.ui.viewmodel.AppState
import icesi.edu.co.fitscan.features.workout.data.api.WorkoutApiService
import icesi.edu.co.fitscan.features.workout.data.api.WorkoutExerciseApiService
import icesi.edu.co.fitscan.features.workout.data.dataSources.impl.WorkoutServiceImpl
import icesi.edu.co.fitscan.features.workout.data.repositories.impl.WorkoutExerciseRepositoryImpl
import icesi.edu.co.fitscan.features.workout.data.repositories.impl.WorkoutRepositoryImpl
import icesi.edu.co.fitscan.features.workout.domain.mapper.WorkoutExerciseMapper
import icesi.edu.co.fitscan.features.workout.domain.mapper.WorkoutMapper
import icesi.edu.co.fitscan.features.workout.domain.usecase.CurrentExercise
import icesi.edu.co.fitscan.features.workout.domain.usecase.NextExercise
import icesi.edu.co.fitscan.features.workout.domain.usecase.PerformWorkoutUseCase
import icesi.edu.co.fitscan.features.workout.domain.usecase.RemainingExercise
import icesi.edu.co.fitscan.features.workout.domain.usecase.WorkoutUiState
import icesi.edu.co.fitscan.features.workout.ui.model.PerformWorkoutUiState
import icesi.edu.co.fitscan.features.workout.ui.viewmodel.PerformWorkoutViewModel
import icesi.edu.co.fitscan.features.workout.ui.viewmodel.factory.PerformWorkoutViewModelFactory
import icesi.edu.co.fitscan.ui.theme.Dimensions
import icesi.edu.co.fitscan.ui.theme.backgroundGrey
import icesi.edu.co.fitscan.ui.theme.greenLess
import icesi.edu.co.fitscan.ui.theme.greyButton
import icesi.edu.co.fitscan.ui.theme.greyMed
import icesi.edu.co.fitscan.ui.theme.greyTrueLight
import icesi.edu.co.fitscan.ui.theme.redDangerous

@Composable
fun PerformWorkoutScreen(
    modifier: Modifier = Modifier,
    performWorkoutUseCase: PerformWorkoutUseCase
) {
    val viewModel: PerformWorkoutViewModel = viewModel(
        factory = PerformWorkoutViewModelFactory(performWorkoutUseCase)
    )

    Log.e("PerformWorkoutScreen", "Customer ID: ${AppState.customerId}")
    // Call startWorkout only once when the screen is shown
    LaunchedEffect(AppState.customerId) {
        viewModel.startWorkout(AppState.customerId.toString())
    }

    val uiState by viewModel.uiState.collectAsState()

    PerformWorkoutScreenContent(
        modifier = modifier,
        viewModel = viewModel,
        uiState = uiState,
        onEndSet = { viewModel.endSet() },
        onSkipToNextExercise = { viewModel.skipToNextExercise() },
        onFinishWorkout = { viewModel.finishWorkout() }
    )
}

@Composable
fun PerformWorkoutListComponent(title: String = "NA", sets: String = "NA", reps: String = "NA") {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Dimensions.MediumCornerRadius))
            .border(
                width = 2.dp,
                color = greyMed,
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
                color = Color.White,
                fontSize = Dimensions.LargeTextSize,
            )
            Text(
                text = "$sets sets ~ $reps reps",
                color = greenLess,
                fontSize = Dimensions.SmallTextSize,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {},
            shape = RectangleShape,
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(containerColor = greyMed),
            modifier = Modifier
                .size(Dimensions.LargeIconSize)
                .clip(RoundedCornerShape(Dimensions.SmallCornerRadius))
        ) {
            Icon(
                painter = painterResource(id = R.drawable.chevron_right),
                contentDescription = "Motrar ejercicio",
                tint = greenLess,
                modifier = Modifier
                    .size(Dimensions.LargeIconSize)
            )
        }
    }
}

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
                modifier.background(backgroundGrey)
            ) {
                item {
                    // Header, subtitles and progress
                    Spacer(modifier = Modifier.height(Dimensions.SmallPadding))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Dimensions.MediumPadding)
                    ) {
                        Text(
                            text = data.title,
                            fontSize = Dimensions.XLargeTextSize,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(Dimensions.SmallPadding))
                        Text(
                            text = data.subtitle,
                            fontSize = Dimensions.SmallTextSize,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(Dimensions.SmallPadding))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(Dimensions.MediumCornerRadius))
                                .background(greyMed)
                                .padding(Dimensions.SmallPadding)
                        ) {
                            Text(
                                text = data.progress,
                                fontSize = Dimensions.MediumTextSize,
                                color = Color.White
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(Dimensions.SmallPadding))

                    // Current exercise
                    Column(
                        modifier = Modifier.padding(horizontal = Dimensions.MediumPadding)
                    ) {
                        Text(
                            text = "Ejercicio actual",
                            color = Color.Gray,
                            fontSize = Dimensions.SmallTextSize,
                        )
                        Spacer(modifier = Modifier.height(Dimensions.SmallPadding))
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(Dimensions.MediumCornerRadius))
                                    .background(greyTrueLight)
                                    .padding(Dimensions.SmallPadding)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .wrapContentSize()
                                        .fillMaxWidth()
                                ) {
                                    Text(
                                        text = data.currentExercise.name,
                                        fontSize = Dimensions.LargeTextSize,
                                        color = Color.White,
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(
                                        text = data.currentExercise.time,
                                        fontSize = Dimensions.LargeTextSize,
                                        color = greenLess,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(Dimensions.SmallPadding))
                                Text(
                                    text = "Serie ${data.currentExercise.series}",
                                    color = greenLess
                                )
                                Spacer(modifier = Modifier.height(Dimensions.SmallPadding))
                                Text(
                                    text = data.currentExercise.remainingTime,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    // Next exercise
                    Column(
                        modifier = Modifier.padding(Dimensions.MediumPadding)
                    ) {
                        Text(
                            text = "Siguiente ejercicio",
                            color = Color.White
                        )
                        Text(
                            text = data.nextExercise.name,
                            color = Color.White,
                            fontSize = Dimensions.LargeTextSize,
                            fontWeight = FontWeight.Bold
                        )
                        Row {
                            Text(
                                text = "${data.nextExercise.sets} sets | ${data.nextExercise.reps} reps",
                                color = Color.White,
                            )
                        }

                        // Workout controls
                        Spacer(modifier = Modifier.height(Dimensions.smallestPadding))
                        Button(
                            onClick = { viewModel.endSet() },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = greenLess),
                        ) {
                            Text(
                                text = "Terminar set",
                                fontSize = Dimensions.MediumTextSize
                            )
                        }
                        Spacer(modifier = Modifier.height(Dimensions.smallestPadding))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(
                                onClick = { /* Previous logic */ },
                                shape = RectangleShape,
                                modifier = Modifier
                                    .size(Dimensions.LargeIconSize)
                                    .clip(RoundedCornerShape(Dimensions.MediumCornerRadius)),
                                contentPadding = PaddingValues(0.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = greyButton),
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_back),
                                    contentDescription = "Anterior",
                                    modifier = Modifier.size(Dimensions.MediumIconSize),
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Button(
                                onClick = { /* Pause logic */ },
                                shape = RectangleShape,
                                modifier = Modifier
                                    .size(Dimensions.LargeIconSize)
                                    .clip(RoundedCornerShape(Dimensions.MediumCornerRadius)),
                                contentPadding = PaddingValues(0.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = greyButton),
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.pause_outline_filled),
                                    contentDescription = "Detener workout",
                                    modifier = Modifier.size(Dimensions.MediumIconSize)
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Button(
                                onClick = { viewModel.skipToNextExercise() },
                                shape = RectangleShape,
                                modifier = Modifier
                                    .size(Dimensions.LargeIconSize)
                                    .clip(RoundedCornerShape(Dimensions.MediumCornerRadius)),
                                contentPadding = PaddingValues(0.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = greyButton),
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_back),
                                    contentDescription = "Siguiente",
                                    modifier = Modifier
                                        .size(Dimensions.MediumIconSize)
                                        .graphicsLayer(scaleX = -1f)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(Dimensions.smallestPadding))
                    }
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
                            reps = exercise.reps
                        )
                    }
                    Spacer(modifier = Modifier.height(Dimensions.SmallPadding))
                }

                // Finish workout button
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = { viewModel.finishWorkout() },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = redDangerous),
                            border = BorderStroke(2.dp, redDangerous)
                        ) {
                            Text(
                                text = "Terminar entrenamiento",
                                fontSize = Dimensions.MediumTextSize,
                                color = redDangerous
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
fun PerformWorkoutScreenPreview() {
    val workoutApiService = RetrofitInstance.create(WorkoutApiService::class.java)
    val workoutExerciseApiService = RetrofitInstance.create(WorkoutExerciseApiService::class.java)
    val workoutMapper = WorkoutMapper()
    val workoutExerciseMapper = WorkoutExerciseMapper()

    val workoutRepository = WorkoutRepositoryImpl(workoutApiService, workoutMapper)
    val workoutExerciseRepository =
        WorkoutExerciseRepositoryImpl(workoutExerciseApiService, workoutExerciseMapper)
    val workoutService = WorkoutServiceImpl(workoutRepository, workoutExerciseRepository)
    val performWorkoutUseCase = PerformWorkoutUseCase(workoutService)
    val mockUiState = PerformWorkoutUiState.Success(
        data = WorkoutUiState(
            title = "Full Body Workout",
            subtitle = "Intermediate · 45 min",
            progress = "2/5 exercises completed",
            currentExercise = CurrentExercise(
                name = "Push Ups",
                time = "00:45",
                series = "2/4",
                remainingTime = "Rest: 00:30"
            ),
            nextExercise = NextExercise(
                name = "Squats",
                sets = 3,
                reps = 12
            ),
            remainingExercises = listOf(
                RemainingExercise("Pull Ups", "3", "8"),
                RemainingExercise("Lunges", "3", "10"),
                RemainingExercise("Plank", "2", "60s")
            )
        )
    )
    PerformWorkoutScreen(
        modifier = Modifier,
        performWorkoutUseCase = performWorkoutUseCase
    )
}