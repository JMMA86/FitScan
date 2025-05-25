package icesi.edu.co.fitscan.features.workout.ui.model


sealed interface PerformWorkoutUiState {
    data object Idle : PerformWorkoutUiState
    data object Loading : PerformWorkoutUiState
    data class Success(val data: WorkoutUiState) : PerformWorkoutUiState
    data class Error(val message: String) : PerformWorkoutUiState
}