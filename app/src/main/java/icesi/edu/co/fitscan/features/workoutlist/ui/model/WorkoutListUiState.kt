package icesi.edu.co.fitscan.features.workoutlist.ui.model

import icesi.edu.co.fitscan.domain.model.Workout

sealed class WorkoutListUiState {
    data object Idle : WorkoutListUiState()
    data object Loading : WorkoutListUiState()
    data class Success(val workouts: List<Workout>) : WorkoutListUiState()
    data class Error(val message: String) : WorkoutListUiState()
}