package icesi.edu.co.fitscan.features.workout.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import icesi.edu.co.fitscan.features.workout.data.model.Exercise
import icesi.edu.co.fitscan.features.workout.data.repository.ExerciseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ExerciseDetailState {
    object Loading : ExerciseDetailState()
    data class Success(val exercise: Exercise) : ExerciseDetailState()
    data class Error(val message: String) : ExerciseDetailState()
}

class ExerciseDetailViewModel(
    private val repository: ExerciseRepository = ExerciseRepository()
) : ViewModel() {
    private val _state = MutableStateFlow<ExerciseDetailState>(ExerciseDetailState.Loading)
    val state: StateFlow<ExerciseDetailState> = _state

    fun loadExercise(exerciseId: String) {
        viewModelScope.launch {
            _state.value = ExerciseDetailState.Loading
            try {
                val response = repository.getExerciseById(exerciseId)
                if (response.isSuccessful && response.body() != null) {
                    _state.value = ExerciseDetailState.Success(response.body()!!)
                } else {
                    _state.value = ExerciseDetailState.Error("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                _state.value = ExerciseDetailState.Error(e.localizedMessage ?: "Error desconocido")
            }
        }
    }

    fun loadExerciseFromWorkoutExercise(workoutExerciseId: String) {
        viewModelScope.launch {
            _state.value = ExerciseDetailState.Loading
            try {
                val workoutExercise = repository.getWorkoutExerciseById(workoutExerciseId)
                val exercise = workoutExercise?.exercise
                if (exercise != null) {
                    _state.value = ExerciseDetailState.Success(exercise)
                } else {
                    _state.value = ExerciseDetailState.Error("No se encontró el ejercicio relacionado")
                }
            } catch (e: Exception) {
                _state.value = ExerciseDetailState.Error(e.localizedMessage ?: "Error desconocido")
            }
        }
    }
}
