package icesi.edu.co.fitscan.features.workout.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import icesi.edu.co.fitscan.features.workout.domain.data.remote.response.NextExercise
import icesi.edu.co.fitscan.features.workout.domain.data.remote.response.WorkoutUiState
import icesi.edu.co.fitscan.features.workout.domain.usecase.PerformWorkoutUseCase
import icesi.edu.co.fitscan.features.workout.ui.model.PerformWorkoutUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PerformWorkoutViewModel(
    private val performWorkoutUseCase: PerformWorkoutUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<PerformWorkoutUiState>(PerformWorkoutUiState.Idle)
    val uiState: StateFlow<PerformWorkoutUiState> = _uiState

    private var workoutState = WorkoutUiState()

    fun startWorkout(customerId: String) {
        _uiState.value = PerformWorkoutUiState.Loading
        viewModelScope.launch {
            val result = performWorkoutUseCase(customerId)
            result.onSuccess { workoutResponse ->
                // Map workoutResponse to WorkoutUiState as needed
                // For now, just use the default
                workoutState = WorkoutUiState(
                    title = workoutResponse.data.firstOrNull()?.name ?: "Workout",
                    // Map other fields as needed
                )
                _uiState.value = PerformWorkoutUiState.Success(workoutState)
            }.onFailure { e ->
                _uiState.value = PerformWorkoutUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun endSet() {
        viewModelScope.launch {
            val current = workoutState.currentExercise
            val completed = current.series.split(" ")[0].toInt() + 1
            val total = current.series.split(" ")[2]
            val updatedSeries = "$completed de $total"
            workoutState = workoutState.copy(
                currentExercise = current.copy(series = updatedSeries),
                progress = updateProgress()
            )
            _uiState.value = PerformWorkoutUiState.Success(workoutState)
        }
    }

    fun skipToNextExercise() {
        viewModelScope.launch {
            val updatedList = workoutState.remainingExercises.drop(1)
            val next = updatedList.getOrNull(0)?.let {
                NextExercise(it.title, it.sets.toInt(), it.reps.toInt())
            } ?: NextExercise()
            workoutState = workoutState.copy(
                nextExercise = next,
                remainingExercises = updatedList
            )
            _uiState.value = PerformWorkoutUiState.Success(workoutState)
        }
    }

    fun finishWorkout() {
        _uiState.value = PerformWorkoutUiState.Idle
    }

    private fun updateProgress(): String {
        val completed = workoutState.progress.split("/")[0].toInt() + 1
        val total = workoutState.progress.split("/")[1].split(" ")[0].toInt()
        return "$completed/$total ejercicios completados"
    }
}