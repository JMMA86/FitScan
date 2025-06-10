package icesi.edu.co.fitscan.features.mealPlan.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import icesi.edu.co.fitscan.domain.model.MealPlan
import icesi.edu.co.fitscan.features.mealPlan.data.repositories.IMealPlanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MealPlanViewModel(private val repository: IMealPlanRepository) : ViewModel() {
    private val _mealPlans = MutableStateFlow<List<MealPlan>>(emptyList())
    val mealPlans: StateFlow<List<MealPlan>> = _mealPlans

    private val _selectedMealPlan = MutableStateFlow<MealPlan?>(null)
    val selectedMealPlan: StateFlow<MealPlan?> = _selectedMealPlan

    fun loadMealPlans() {
        viewModelScope.launch {
            _mealPlans.value = repository.getMealPlans()
        }
    }

    fun selectMealPlan(id: String) {
        viewModelScope.launch {
            _selectedMealPlan.value = repository.getMealPlanById(id)
        }
    }

    fun createMealPlan(mealPlan: MealPlan) {
        viewModelScope.launch {
            if (repository.createMealPlan(mealPlan)) loadMealPlans()
        }
    }

    fun updateMealPlan(mealPlan: MealPlan) {
        viewModelScope.launch {
            if (repository.updateMealPlan(mealPlan)) loadMealPlans()
        }
    }

    fun deleteMealPlan(id: String) {
        viewModelScope.launch {
            if (repository.deleteMealPlan(id)) loadMealPlans()
        }
    }
} 