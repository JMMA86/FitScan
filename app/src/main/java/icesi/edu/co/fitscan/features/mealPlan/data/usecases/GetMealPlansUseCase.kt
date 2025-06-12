package icesi.edu.co.fitscan.features.mealPlan.data.usecases

import icesi.edu.co.fitscan.features.mealPlan.data.dto.MealPlanDto
import icesi.edu.co.fitscan.features.mealPlan.data.repositories.MealPlanRepository

class GetMealPlansUseCase(private val repository: MealPlanRepository) {
    suspend operator fun invoke(): List<MealPlanDto> {
        return repository.getMealPlans()
    }
} 