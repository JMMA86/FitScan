package icesi.edu.co.fitscan.features.workoutlist.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import icesi.edu.co.fitscan.domain.model.Workout
import icesi.edu.co.fitscan.domain.usecases.IGetWorkoutsUseCase
import icesi.edu.co.fitscan.features.common.ui.viewmodel.AppState
import icesi.edu.co.fitscan.features.workoutlist.ui.model.WorkoutListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class WorkoutListViewModel(
    private val getWorkoutsUseCase: IGetWorkoutsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<WorkoutListUiState>(WorkoutListUiState.Loading)
    val uiState: StateFlow<WorkoutListUiState> = _uiState

    private var allWorkouts: List<Workout> = emptyList()

    init {
        loadWorkouts()
    }

    private fun loadWorkouts() {
        _uiState.value = WorkoutListUiState.Loading
        viewModelScope.launch {
            val customerId = AppState.customerId?.let { UUID.fromString(it) }
            val result = getWorkoutsUseCase(customerId)
            result.onSuccess { workouts ->
                allWorkouts = workouts
                _uiState.value = WorkoutListUiState.Success(workouts)
            }.onFailure { error ->
                _uiState.value = WorkoutListUiState.Error(error.message ?: "Error al cargar rutinas")
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        if (_uiState.value is WorkoutListUiState.Success) {
            val filtered = allWorkouts.filter { it.name.contains(query, ignoreCase = true) }
            _uiState.value = WorkoutListUiState.Success(filtered)
        }
    }
}