package icesi.edu.co.fitscan.features.workout.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import icesi.edu.co.fitscan.domain.model.Workout
import icesi.edu.co.fitscan.domain.model.WorkoutExercise
import icesi.edu.co.fitscan.domain.repositories.IExerciseRepository
import icesi.edu.co.fitscan.domain.repositories.IWorkoutExerciseRepository
import icesi.edu.co.fitscan.domain.repositories.IWorkoutRepository
import icesi.edu.co.fitscan.features.workout.ui.model.UiWorkoutExercise
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

sealed class WorkoutDetailState {
    object Loading : WorkoutDetailState()
    data class Success(
        val workout: Workout,
        val exercises: List<UiWorkoutExercise>
    ) : WorkoutDetailState()
    data class Error(val message: String) : WorkoutDetailState()
}

class WorkoutDetailViewModel(
    private val workoutRepository: IWorkoutRepository,
    private val workoutExerciseRepository: IWorkoutExerciseRepository,
    private val exerciseRepository: IExerciseRepository // <-- Add this dependency
) : ViewModel() {
    private val _state = MutableStateFlow<WorkoutDetailState>(WorkoutDetailState.Loading)
    val state: StateFlow<WorkoutDetailState> = _state

    fun loadWorkout(workoutId: String) {        viewModelScope.launch {
            _state.value = WorkoutDetailState.Loading
            try {
                val id = UUID.fromString(workoutId)
                val workoutResult = workoutRepository.getWorkoutById(id)
                val exercisesResult = workoutExerciseRepository.getWorkoutExercises(id)
                val allExercisesResult = exerciseRepository.getAllExercises()
                
                Log.d("WorkoutDetailVM", "workoutId: $workoutId")
                Log.d("WorkoutDetailVM", "workoutResult isSuccess: ${workoutResult.isSuccess}")
                Log.d("WorkoutDetailVM", "exercisesResult isSuccess: ${exercisesResult.isSuccess}")
                Log.d("WorkoutDetailVM", "allExercisesResult isSuccess: ${allExercisesResult.isSuccess}")
                
                if (workoutResult.isFailure) {
                    Log.e("WorkoutDetailVM", "Workout failed: ${workoutResult.exceptionOrNull()?.message}")
                }
                if (exercisesResult.isFailure) {
                    Log.e("WorkoutDetailVM", "Exercises failed: ${exercisesResult.exceptionOrNull()?.message}")
                }
                if (allExercisesResult.isFailure) {
                    Log.e("WorkoutDetailVM", "All exercises failed: ${allExercisesResult.exceptionOrNull()?.message}")
                }
                
                if (workoutResult.isSuccess && exercisesResult.isSuccess && allExercisesResult.isSuccess) {
                    val allExercises = allExercisesResult.getOrThrow()
                    val exerciseMap = allExercises.associateBy { it.id }
                    val uiExercises = exercisesResult.getOrThrow().map { ex ->
                        UiWorkoutExercise(
                            id = ex.id.toString(),
                            name = exerciseMap[ex.exerciseId]?.name ?: ex.exerciseId.toString(),
                            sets = ex.sets,
                            reps = ex.reps
                        )
                    }
                    _state.value = WorkoutDetailState.Success(
                        workout = workoutResult.getOrThrow(),
                        exercises = uiExercises
                    )                } else {
                    val errorMessage = when {
                        workoutResult.isFailure -> "Error al cargar la rutina: ${workoutResult.exceptionOrNull()?.message}"
                        exercisesResult.isFailure -> "Error al cargar los ejercicios de la rutina: ${exercisesResult.exceptionOrNull()?.message}"
                        allExercisesResult.isFailure -> "Error al cargar la información de ejercicios: ${allExercisesResult.exceptionOrNull()?.message}"
                        else -> "No se pudo cargar la rutina o sus ejercicios"
                    }
                    Log.e("WorkoutDetailVM", "Failed to load workout data: $errorMessage")
                    _state.value = WorkoutDetailState.Error(errorMessage)
                }
            } catch (e: Exception) {
                Log.e("WorkoutDetailVM", "Exception: ${e.localizedMessage}", e)
                _state.value = WorkoutDetailState.Error(e.localizedMessage ?: "Error desconocido")
            }
        }
    }

    fun loadFirstWorkout() {
        // Implementar si se requiere lógica para cargar la primera rutina
    }
}
