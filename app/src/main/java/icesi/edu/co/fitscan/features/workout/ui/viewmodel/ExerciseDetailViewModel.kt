package icesi.edu.co.fitscan.features.workout.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import icesi.edu.co.fitscan.domain.model.Exercise
import icesi.edu.co.fitscan.domain.repositories.IExerciseRepository
import icesi.edu.co.fitscan.domain.repositories.IWorkoutExerciseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

sealed class ExerciseDetailState {
    object Loading : ExerciseDetailState()
    data class Success(val exercise: Exercise) : ExerciseDetailState()
    data class Error(val message: String) : ExerciseDetailState()
}

class ExerciseDetailViewModel(
    private val exerciseRepository: IExerciseRepository,
    private val workoutExerciseRepository: IWorkoutExerciseRepository
) : ViewModel() {    private val _state = MutableStateFlow<ExerciseDetailState>(ExerciseDetailState.Loading)
    val state: StateFlow<ExerciseDetailState> = _state

    fun loadExerciseDetail(workoutId: UUID, workoutExerciseId: UUID) {
        viewModelScope.launch {
            Log.d("ExerciseDetailVM", "Cargando detalle para workoutId=$workoutId, workoutExerciseId=$workoutExerciseId")
            _state.value = ExerciseDetailState.Loading
            
            val result = workoutExerciseRepository.getWorkoutExerciseById(workoutId, workoutExerciseId)
            result.onSuccess { workoutExercise ->
                Log.d("ExerciseDetailVM", "WorkoutExercise encontrado: $workoutExercise")
                val exerciseResult = exerciseRepository.getExerciseById(workoutExercise.exerciseId)
                if (exerciseResult.isSuccess) {
                    val exercise = exerciseResult.getOrNull()
                    Log.d("ExerciseDetailVM", "Exercise encontrado: $exercise")
                    if (exercise != null) {
                        Log.d("ExerciseDetailVM", "Exercise fields: id=${exercise.id}, name=${exercise.name}, description='${exercise.description}', muscleGroups='${exercise.muscleGroups}'")
                        _state.value = ExerciseDetailState.Success(exercise)
                    } else {
                        Log.e("ExerciseDetailVM", "El objeto Exercise es null para id: ${workoutExercise.exerciseId}")
                        _state.value = ExerciseDetailState.Error("No se pudo cargar el ejercicio (objeto nulo) con id: ${workoutExercise.exerciseId}")
                    }
                } else {
                    Log.e("ExerciseDetailVM", "Error al obtener Exercise: ${exerciseResult.exceptionOrNull()?.message}")
                    _state.value = ExerciseDetailState.Error("No se pudo cargar el ejercicio con id: ${workoutExercise.exerciseId}. Error: ${exerciseResult.exceptionOrNull()?.message}")
                }
            }.onFailure { error ->
                Log.e("ExerciseDetailVM", "Error al obtener WorkoutExercise: ${error.message}")
                _state.value = ExerciseDetailState.Error("Error al obtener la relación WorkoutExercise: ${error.message ?: "Error desconocido"}")
            }
        }
    }
}
