package icesi.edu.co.fitscan.features.workout.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import icesi.edu.co.fitscan.domain.model.Exercise
import icesi.edu.co.fitscan.domain.usecases.ICreateExerciseUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CreateExerciseViewModel(
    private val createExerciseUseCase: ICreateExerciseUseCase
) : ViewModel() {

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving

    private val _saveSuccess = MutableStateFlow<Exercise?>(null)
    val saveSuccess: StateFlow<Exercise?> = _saveSuccess

    private val _saveError = MutableStateFlow<String?>(null)
    val saveError: StateFlow<String?> = _saveError

    fun createExercise(exercise: Exercise) {
        _isSaving.value = true
        _saveSuccess.value = null
        _saveError.value = null
        viewModelScope.launch {
            createExerciseUseCase(exercise)
                .onSuccess { createdExercise ->
                    _saveSuccess.value = createdExercise
                }
                .onFailure { error ->
                    _saveError.value = error.message ?: "Error al crear el ejercicio"
                }
            _isSaving.value = false
        }
    }

    fun resetSaveState() {
        _saveSuccess.value = null
        _saveError.value = null
    }
}
