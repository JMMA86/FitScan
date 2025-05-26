package icesi.edu.co.fitscan.features.workout.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import icesi.edu.co.fitscan.domain.usecases.IManageExercisesUseCase
import icesi.edu.co.fitscan.domain.usecases.IManageWorkoutExercisesUseCase
import icesi.edu.co.fitscan.domain.usecases.IManageWorkoutUseCase
import icesi.edu.co.fitscan.features.workout.ui.model.CurrentExercise
import icesi.edu.co.fitscan.features.workout.ui.model.NextExercise
import icesi.edu.co.fitscan.features.workout.ui.model.PerformWorkoutUiState
import icesi.edu.co.fitscan.features.workout.ui.model.RemainingExercise
import icesi.edu.co.fitscan.features.workout.ui.model.WorkoutUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class PerformWorkoutViewModel(
    private val performWorkoutUseCase: IManageWorkoutExercisesUseCase,
    private val exerciseUseCase: IManageExercisesUseCase,
    private val workoutUseCase: IManageWorkoutUseCase,
    private val workoutId: String
) : ViewModel() {
    private val _uiState = MutableStateFlow<PerformWorkoutUiState>(PerformWorkoutUiState.Idle)
    val uiState: StateFlow<PerformWorkoutUiState> get() = _uiState
    var actualExerciseId: Int = 0

    private var _workoutState = WorkoutUiState()

    fun startWorkout() {
        _uiState.value = PerformWorkoutUiState.Loading
        viewModelScope.launch {
            val workoutId = UUID.fromString(workoutId)
            val exercisesResponse = performWorkoutUseCase.getWorkoutExercises(workoutId)
            val exercises = mutableListOf<RemainingExercise>()
            for (item in exercisesResponse.getOrNull().orEmpty()) {
                val exercise = exerciseUseCase.getExerciseById(item.exerciseId)

                exercises.add(
                    RemainingExercise(
                        title = exercise.name.toString(),
                        sets = item.sets.toString(),
                        reps = item.reps.toString()
                    )
                )
            }
            val firstWorkoutId = exercisesResponse.getOrNull()?.firstOrNull()?.workoutId
            var actualWorkout =
                firstWorkoutId?.let { workoutUseCase.getWorkoutById(it).getOrNull() }

            val currentTime = LocalTime.now()
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            val formattedTime = currentTime.format(formatter)

            val totalMinutes = actualWorkout?.durationMinutes ?: 0
            val hours = totalMinutes / 60
            val minutes = totalMinutes % 60

            val currentExercise: CurrentExercise = (exercises.getOrNull(actualExerciseId)?.let {
                CurrentExercise(
                    it.title,
                    formattedTime,
                    it.sets,
                    "Duración total: $hours h : $minutes m"
                )
            } ?: NextExercise()) as CurrentExercise

            actualExerciseId++

            val nextExercise = exercises.getOrNull(actualExerciseId)?.let {
                NextExercise(it.title, it.sets.toInt(), it.reps.toInt())
            } ?: NextExercise()

            actualExerciseId++

            _workoutState = _workoutState.copy(
                title = actualWorkout?.name.toString(),
                subtitle = actualWorkout?.type.toString(),
                progress = "0/${exercises.size} ejercicios completados",
                currentExercise = currentExercise,
                nextExercise = nextExercise,
                remainingExercises = exercises
            )

            _uiState.value = PerformWorkoutUiState.Success(_workoutState)
        }
    }

    fun endSet() {
        viewModelScope.launch {
            val current = _workoutState.currentExercise
            val completed = current.series.split(" ")[0].toInt() + 1
            val total = current.series.split(" ")[2]
            val updatedSeries = "$completed de $total"
            _workoutState = _workoutState.copy(
                currentExercise = current.copy(series = updatedSeries),
                progress = updateProgress()
            )
            _uiState.value = PerformWorkoutUiState.Success(_workoutState)
        }
    }

    fun skipToNextExercise() {
        viewModelScope.launch {
            val updatedList = _workoutState.remainingExercises.drop(1)
            val next = updatedList.getOrNull(0)?.let {
                NextExercise(it.title, it.sets.toInt(), it.reps.toInt())
            } ?: NextExercise()
            _workoutState = _workoutState.copy(
                nextExercise = next,
                remainingExercises = updatedList
            )
            _uiState.value = PerformWorkoutUiState.Success(_workoutState)
        }
    }

    fun finishWorkout() {
        _uiState.value = PerformWorkoutUiState.Idle
    }

    private fun updateProgress(): String {
        val completed = _workoutState.progress.split("/")[0].toInt() + 1
        val total = _workoutState.progress.split("/")[1].split(" ")[0].toInt()
        return "$completed/$total ejercicios completados"
    }
}