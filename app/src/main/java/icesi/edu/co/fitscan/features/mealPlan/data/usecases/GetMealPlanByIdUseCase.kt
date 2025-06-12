package icesi.edu.co.fitscan.features.mealPlan.data.usecases

import icesi.edu.co.fitscan.features.mealPlan.data.dto.MealPlanDto
import icesi.edu.co.fitscan.features.mealPlan.data.repositories.MealPlanRepository

class GetMealPlanByIdUseCase(private val repository: MealPlanRepository) {
    suspend operator fun invoke(id: String): MealPlanDto? {
        return repository.getMealPlanById(id)
    }
} 