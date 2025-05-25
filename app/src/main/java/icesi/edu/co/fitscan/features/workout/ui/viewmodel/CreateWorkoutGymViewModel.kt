package icesi.edu.co.fitscan.features.workout.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import icesi.edu.co.fitscan.domain.model.Exercise
import icesi.edu.co.fitscan.domain.model.Workout
import icesi.edu.co.fitscan.domain.model.WorkoutExercise
import icesi.edu.co.fitscan.domain.model.WorkoutType
import icesi.edu.co.fitscan.domain.usecases.IManageExercisesUseCase
import icesi.edu.co.fitscan.domain.usecases.IManageWorkoutUseCase
import icesi.edu.co.fitscan.features.common.ui.viewmodel.AppState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.util.UUID

class CreateWorkoutGymViewModel(
    private val getExercisesUseCase: IManageExercisesUseCase,
    private val createWorkoutUseCase: IManageWorkoutUseCase
) : ViewModel() {

    private val _exercises = MutableStateFlow<List<Exercise>>(emptyList())
    val exercises: StateFlow<List<Exercise>> = _exercises

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _requestDetails = MutableStateFlow<String?>(null)

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving

    private val _saveSuccess = MutableStateFlow<Workout?>(null)
    val saveSuccess: StateFlow<Workout?> = _saveSuccess

    private val _saveError = MutableStateFlow<String?>(null)
    val saveError: StateFlow<String?> = _saveError

    private val customerId = AppState.customerId ?: ""

    init {
        loadExercises()
    }

    private fun loadExercises() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _requestDetails.value = null

            try {
                getExercisesUseCase()
                    .catch { e ->
                        val errorMessage = when {
                            e.message?.contains("403") == true ->
                                "Error de autorización (403). Verifica que tienes un token válido."

                            e.message?.contains("Unable to resolve host") == true ->
                                "No se pudo conectar al servidor. Verifica tu conexión a Internet."

                            e.message?.contains("timeout") == true ->
                                "Tiempo de espera agotado. El servidor está tardando en responder."

                            else -> e.message ?: "Error desconocido al cargar ejercicios"
                        }
                        _error.value = errorMessage
                        _requestDetails.value = "Error completo: ${e.message}\n" +
                                "Causa: ${e.cause}\n" +
                                "Stack trace: ${e.stackTraceToString()}"
                        _isLoading.value = false
                    }
                    .collect { exercises ->
                        _exercises.value = exercises
                        _isLoading.value = false
                    }
            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("403") == true ->
                        "Error de autorización (403). Verifica que tienes un token válido."

                    e.message?.contains("Unable to resolve host") == true ->
                        "No se pudo conectar al servidor. Verifica tu conexión a Internet."

                    e.message?.contains("timeout") == true ->
                        "Tiempo de espera agotado. El servidor está tardando en responder."

                    else -> e.message ?: "Error desconocido al cargar ejercicios"
                }
                _error.value = errorMessage
                _requestDetails.value = "Error completo: ${e.message}\n" +
                        "Causa: ${e.cause}\n" +
                        "Stack trace: ${e.stackTraceToString()}"
                _isLoading.value = false
            }
        }
    }

    // Función para crear workout y sus ejercicios
    fun createWorkout(name: String, exerciseList: List<Pair<String, Pair<Int, Int>>>) {
        if (name.isBlank()) {
            _saveError.value = "El nombre del entrenamiento no puede estar vacío"
            return
        }

        if (exerciseList.isEmpty()) {
            _saveError.value = "Debes agregar al menos un ejercicio"
            return
        }

        viewModelScope.launch {
            _isSaving.value = true
            _saveSuccess.value = null
            _saveError.value = null

            try {
                // Crear el objeto Workout
                val workout = Workout(
                    id = UUID.randomUUID(),
                    customerId = if (customerId != "") UUID.fromString(customerId) else UUID.fromString(
                        "32db6c2b-c79c-4a0e-adb4-0809a8ecb1a4"
                    ), // Quemado hasta que se solucione lo de los roles
                    name = name,
                    type = WorkoutType.Gym,
                    durationMinutes = 60,
                    difficulty = "Easy"
                )

                // Preparar la lista de WorkoutExercise
                val workoutExercises = mutableListOf<WorkoutExercise>()

                for ((exerciseName, setsReps) in exerciseList) {
                    val (sets, reps) = setsReps

                    // Buscar el ID del ejercicio por su nombre
                    val exercise = _exercises.value.find { it.name == exerciseName }

                    if (exercise?.id != null) {
                        workoutExercises.add(
                            WorkoutExercise(
                                id = UUID.randomUUID(),
                                workoutId = workout.id,
                                exerciseId = exercise.id,
                                sets = sets,
                                reps = reps,
                                isAiSuggested = false
                            )
                        )
                    }
                }

                if (workoutExercises.isEmpty()) {
                    _saveError.value = "No se pudieron encontrar los ejercicios seleccionados"
                    _isSaving.value = false
                    return@launch
                }

                // Usar el caso de uso para crear el workout con sus ejercicios
                createWorkoutUseCase(workout, workoutExercises)
                    .onSuccess { createdWorkout ->
                        _saveSuccess.value = createdWorkout
                    }
                    .onFailure { error ->
                        _saveError.value = error.message ?: "Error al guardar el entrenamiento"
                    }

            } catch (e: Exception) {
                _saveError.value = e.message ?: "Error inesperado al crear el entrenamiento"
            } finally {
                _isSaving.value = false
            }
        }
    }

    fun resetSaveState() {
        _saveSuccess.value = null
        _saveError.value = null
    }

    fun reload() {
        resetSaveState()
        loadExercises()
    }
}