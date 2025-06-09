package icesi.edu.co.fitscan.features.statistics.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import icesi.edu.co.fitscan.domain.model.MuscleGroup
import icesi.edu.co.fitscan.domain.model.MuscleGroupProgress
import icesi.edu.co.fitscan.domain.usecases.IFetchAllMuscleGroupsUseCase
import icesi.edu.co.fitscan.domain.usecases.IFetchMuscleGroupProgressUseCase
import icesi.edu.co.fitscan.features.common.ui.viewmodel.AppState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class MuscleGroupProgressUiState {
    object Loading : MuscleGroupProgressUiState()
    data class Success(
        val muscleGroups: List<MuscleGroup>,
        val progressData: List<MuscleGroupProgress>
    ) : MuscleGroupProgressUiState()
    data class Error(val message: String) : MuscleGroupProgressUiState()
}

class MuscleGroupProgressViewModel(
    private val fetchAllMuscleGroupsUseCase: IFetchAllMuscleGroupsUseCase,
    private val fetchMuscleGroupProgressUseCase: IFetchMuscleGroupProgressUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<MuscleGroupProgressUiState>(MuscleGroupProgressUiState.Loading)
    val uiState: StateFlow<MuscleGroupProgressUiState> = _uiState.asStateFlow()

    private val _selectedMuscleGroup = MutableStateFlow<MuscleGroup?>(null)
    val selectedMuscleGroup: StateFlow<MuscleGroup?> = _selectedMuscleGroup.asStateFlow()

    init {
        loadMuscleGroupProgress()
    }

    fun loadMuscleGroupProgress() {
        val customerId = AppState.customerId ?: return
        
        viewModelScope.launch {
            _uiState.value = MuscleGroupProgressUiState.Loading
            
            try {
                // Load muscle groups and progress data in parallel
                val muscleGroupsResult = fetchAllMuscleGroupsUseCase()
                val progressResult = fetchMuscleGroupProgressUseCase(customerId)

                if (muscleGroupsResult.isSuccess && progressResult.isSuccess) {
                    val muscleGroups = muscleGroupsResult.getOrThrow()
                    val progressData = progressResult.getOrThrow()
                    
                    _uiState.value = MuscleGroupProgressUiState.Success(
                        muscleGroups = muscleGroups,
                        progressData = progressData
                    )
                } else {
                    val error = muscleGroupsResult.exceptionOrNull() 
                        ?: progressResult.exceptionOrNull() 
                        ?: Exception("Unknown error")
                    _uiState.value = MuscleGroupProgressUiState.Error(error.message ?: "Error loading data")
                }
            } catch (e: Exception) {
                _uiState.value = MuscleGroupProgressUiState.Error(e.message ?: "Unexpected error")
            }
        }
    }

    fun selectMuscleGroup(muscleGroup: MuscleGroup) {
        _selectedMuscleGroup.value = muscleGroup
    }

    fun clearSelection() {
        _selectedMuscleGroup.value = null
    }

    fun retry() {
        loadMuscleGroupProgress()
    }
}
