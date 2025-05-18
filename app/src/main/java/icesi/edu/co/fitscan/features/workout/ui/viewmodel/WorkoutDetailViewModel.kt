package icesi.edu.co.fitscan.features.workout.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import icesi.edu.co.fitscan.features.workout.data.model.Workout
import icesi.edu.co.fitscan.features.workout.data.model.WorkoutExerciseWithExercise
import icesi.edu.co.fitscan.features.workout.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class WorkoutDetailState {
    object Loading : WorkoutDetailState()
    data class Success(val workout: Workout, val exercises: List<WorkoutExerciseWithExercise>) : WorkoutDetailState()
    data class Error(val message: String) : WorkoutDetailState()
}

class WorkoutDetailViewModel(
    private val repository: WorkoutRepository = WorkoutRepository()
) : ViewModel() {
    private val _state = MutableStateFlow<WorkoutDetailState>(WorkoutDetailState.Loading)
    val state: StateFlow<WorkoutDetailState> = _state

    fun loadFirstWorkout() {
        viewModelScope.launch {
            _state.value = WorkoutDetailState.Loading
            try {
                val workout = repository.getFirstWorkout()
                if (workout != null) {
                    val exercises = repository.getWorkoutExercisesByWorkoutId(workout.id)
                    _state.value = WorkoutDetailState.Success(workout, exercises)
                } else {
                    _state.value = WorkoutDetailState.Error("No hay rutinas disponibles")
                }
            } catch (e: Exception) {
                _state.value = WorkoutDetailState.Error(e.localizedMessage ?: "Error desconocido")
            }
        }
    }

    fun loadWorkout(workoutId: String) {
        viewModelScope.launch {
            _state.value = WorkoutDetailState.Loading
            try {
                val response = repository.getWorkoutById(workoutId)
                if (response.isSuccessful && response.body() != null) {
                    val workout = response.body()!!
                    val exercises = repository.getWorkoutExercisesByWorkoutId(workoutId)
                    _state.value = WorkoutDetailState.Success(workout, exercises)
                } else {
                    _state.value = WorkoutDetailState.Error("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                _state.value = WorkoutDetailState.Error(e.localizedMessage ?: "Error desconocido")
            }
        }
    }
}
