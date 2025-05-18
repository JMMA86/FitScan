package icesi.edu.co.fitscan.features.workout.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class WorkoutUi(
    val name: String,
    val time: String,
    val level: String,
    val exercises: String
)

data class WorkoutListUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val myWorkouts: List<WorkoutUi> = emptyList(),
    val popularWorkouts: List<WorkoutUi> = emptyList()
)

class WorkoutListViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(WorkoutListUiState())
    val uiState: StateFlow<WorkoutListUiState> = _uiState.asStateFlow()

    init {
        loadWorkouts()
    }

    fun loadWorkouts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                // Simulación de carga de datos
                kotlinx.coroutines.delay(1000)
                _uiState.value = WorkoutListUiState(
                    isLoading = false,
                    myWorkouts = listOf(
                        WorkoutUi("Fuerza de cuerpo completo", "45 min", "Avanzado", "8 ejercicios"),
                        WorkoutUi("Carrera matutina", "45 min", "Avanzado", "8 ejercicios"),
                        WorkoutUi("Tren superior", "45 min", "Avanzado", "8 ejercicios")
                    ),
                    popularWorkouts = listOf(
                        WorkoutUi("Sesión HIIT", "45 min", "Avanzado", "8 ejercicios"),
                        WorkoutUi("Carrera matutina", "45 min", "Avanzado", "8 ejercicios"),
                        WorkoutUi("Tren superior", "45 min", "Avanzado", "8 ejercicios")
                    )
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "Error desconocido")
            }
        }
    }

    fun retry() {
        loadWorkouts()
    }
} 