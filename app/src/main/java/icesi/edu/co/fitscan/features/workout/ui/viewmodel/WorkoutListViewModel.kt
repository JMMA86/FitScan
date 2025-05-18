package icesi.edu.co.fitscan.features.workout.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import icesi.edu.co.fitscan.features.workout.domain.data.remote.response.Workout
import icesi.edu.co.fitscan.features.workout.domain.service.WorkoutListService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class WorkoutListUiState {
    object Idle : WorkoutListUiState()
    object Loading : WorkoutListUiState()
    data class Success(val workouts: List<Workout>) : WorkoutListUiState()
    data class Error(val message: String) : WorkoutListUiState()
}

class WorkoutListViewModel(
    private val service: WorkoutListService
) : ViewModel() {

    private val _uiState = MutableStateFlow<WorkoutListUiState>(WorkoutListUiState.Idle)
    val uiState: StateFlow<WorkoutListUiState> = _uiState.asStateFlow()

    init {
        loadWorkouts()
    }

    private fun loadWorkouts() {
        viewModelScope.launch {
            _uiState.value = WorkoutListUiState.Loading
            try {
                val workouts = service.getWorkouts()
                _uiState.value = WorkoutListUiState.Success(workouts)
            } catch (e: Exception) {
                _uiState.value = WorkoutListUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }
} 