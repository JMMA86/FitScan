package icesi.edu.co.fitscan.features.mealPlan.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import icesi.edu.co.fitscan.features.mealPlan.data.dto.MealPlanDto
import icesi.edu.co.fitscan.features.mealPlan.data.repositories.MealPlanRepository
import icesi.edu.co.fitscan.features.mealPlan.data.usecases.GetMealPlansUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MealPlanListViewModel(
    private val repository: MealPlanRepository,
    private val getMealPlansUseCase: GetMealPlansUseCase
) : ViewModel() {
    private val _myPlans = MutableStateFlow<List<MealPlanDto>>(emptyList())
    val myPlans: StateFlow<List<MealPlanDto>> = _myPlans

    private val _popularPlans = MutableStateFlow<List<MealPlanDto>>(emptyList())
    val popularPlans: StateFlow<List<MealPlanDto>> = _popularPlans

    fun loadData() {
        viewModelScope.launch {
            _myPlans.value = getMealPlansUseCase()
            // TODO: Implement logic for popular plans if needed
        }
    }
} 