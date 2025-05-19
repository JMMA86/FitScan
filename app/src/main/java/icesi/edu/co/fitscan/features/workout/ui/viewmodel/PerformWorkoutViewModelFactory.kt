package icesi.edu.co.fitscan.features.workout.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import icesi.edu.co.fitscan.features.workout.domain.usecase.PerformWorkoutUseCase

class PerformWorkoutViewModelFactory(
    private val performWorkoutUseCase: PerformWorkoutUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PerformWorkoutViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PerformWorkoutViewModel(performWorkoutUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}