package icesi.edu.co.fitscan.features.mealPlan.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import icesi.edu.co.fitscan.features.mealPlan.data.dto.MealPlanDto
import icesi.edu.co.fitscan.features.mealPlan.data.usecases.GetMealPlanByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MealPlanDetailViewModel(
    private val getMealPlanByIdUseCase: GetMealPlanByIdUseCase
) : ViewModel() {
    private val _mealPlan = MutableStateFlow<MealPlanDto?>(null)
    val mealPlan: StateFlow<MealPlanDto?> = _mealPlan

    fun loadMealPlan(id: String) {
        viewModelScope.launch {
            _mealPlan.value = getMealPlanByIdUseCase(id)
        }
    }
} 