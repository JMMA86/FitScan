package icesi.edu.co.fitscan.features.workout.ui.model

import icesi.edu.co.fitscan.features.workout.domain.data.remote.response.WorkoutUiState

sealed interface PerformWorkoutUiState {
    object Idle : PerformWorkoutUiState
    object Loading : PerformWorkoutUiState
    data class Success(val data: WorkoutUiState) : PerformWorkoutUiState
    data class Error(val message: String) : PerformWorkoutUiState
}