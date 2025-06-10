package icesi.edu.co.fitscan.features.mealPlan.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import icesi.edu.co.fitscan.features.mealPlan.data.dto.*
import icesi.edu.co.fitscan.features.mealPlan.data.repositories.MealPlanRepository
import icesi.edu.co.fitscan.features.mealPlan.data.usecases.CreateMealPlanUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class CreateMealPlanUiState {
    object Idle : CreateMealPlanUiState()
    object Loading : CreateMealPlanUiState()
    object Success : CreateMealPlanUiState()
    data class Error(val message: String) : CreateMealPlanUiState()
}

class CreateMealPlanViewModel(
    private val repository: MealPlanRepository,
    private val createMealPlanUseCase: CreateMealPlanUseCase,
    private val customerId: String
) : ViewModel() {
    private val _uiState = MutableStateFlow<CreateMealPlanUiState>(CreateMealPlanUiState.Idle)
    val uiState: StateFlow<CreateMealPlanUiState> = _uiState

    private val _goals = MutableStateFlow<List<FitnessGoalDto>>(emptyList())
    val goals: StateFlow<List<FitnessGoalDto>> = _goals

    private val _restrictions = MutableStateFlow<List<DietaryRestrictionDto>>(emptyList())
    val restrictions: StateFlow<List<DietaryRestrictionDto>> = _restrictions

    private val _preferences = MutableStateFlow<List<DietaryPreferenceDto>>(emptyList())
    val preferences: StateFlow<List<DietaryPreferenceDto>> = _preferences

    private val _meals = MutableStateFlow<List<MealDto>>(emptyList())
    val meals: StateFlow<List<MealDto>> = _meals

    fun loadData() {
        viewModelScope.launch {
            _goals.value = repository.getGoals()
            _restrictions.value = repository.getDietaryRestrictions()
            _preferences.value = repository.getDietaryPreferences()
            _meals.value = repository.getMeals()
        }
    }

    fun createMealPlan(
        name: String,
        description: String?,
        goal: String?,
        mealIds: List<String>,
        restrictionIds: List<String>,
        preferenceIds: List<String>
    ) {
        _uiState.value = CreateMealPlanUiState.Loading
        viewModelScope.launch {
            val result = createMealPlanUseCase.invoke(
                customerId, name, description, goal, mealIds, restrictionIds, preferenceIds
            )
            _uiState.value = if (result) CreateMealPlanUiState.Success else CreateMealPlanUiState.Error("No se pudo crear el plan")
        }
    }
}
