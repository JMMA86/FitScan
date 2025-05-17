// File: app/src/main/java/icesi/edu/co/fitscan/features/workout/ui/viewmodel/PerformWorkoutViewModel.kt
package icesi.edu.co.fitscan.features.workout.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import icesi.edu.co.fitscan.features.workout.ui.model.NextExercise
import icesi.edu.co.fitscan.features.workout.ui.model.PerformWorkoutUiState
import icesi.edu.co.fitscan.features.workout.ui.model.WorkoutUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PerformWorkoutViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<PerformWorkoutUiState>(PerformWorkoutUiState.Idle)
    val uiState: StateFlow<PerformWorkoutUiState> = _uiState

    private var workoutState = WorkoutUiState()

    init {
        _uiState.value = PerformWorkoutUiState.Success(workoutState)
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
        // Handle workout finish logic
        _uiState.value = PerformWorkoutUiState.Idle
    }

    private fun updateProgress(): String {
        val completed = workoutState.progress.split("/")[0].toInt() + 1
        val total = workoutState.progress.split("/")[1].split(" ")[0].toInt()
        return "$completed/$total ejercicios completados"
    }
}