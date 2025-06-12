package icesi.edu.co.fitscan.features.mealPlan.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import icesi.edu.co.fitscan.features.mealPlan.data.dto.MealPlanDto
import icesi.edu.co.fitscan.features.mealPlan.data.usecases.GetMealPlanByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import icesi.edu.co.fitscan.features.mealPlan.data.repositories.MealPlanRepository
import icesi.edu.co.fitscan.features.mealPlan.data.dto.MealDto

class MealPlanDetailViewModel(
    private val getMealPlanByIdUseCase: GetMealPlanByIdUseCase
) : ViewModel() {
    private val _mealPlan = MutableStateFlow<MealPlanDto?>(null)
    val mealPlan: StateFlow<MealPlanDto?> = _mealPlan

    private val _meals = MutableStateFlow<List<MealDto>>(emptyList())
    val meals: StateFlow<List<MealDto>> = _meals

    fun loadMealPlanAndMeals(mealPlanId: String, repository: MealPlanRepository) {
        viewModelScope.launch {
            val plan = getMealPlanByIdUseCase(mealPlanId)
            _mealPlan.value = plan
            val mealPlanMeals = repository.getMealsForMealPlan(mealPlanId)
            _meals.value = mealPlanMeals.mapNotNull { it.meal_id }
        }
    }
} 