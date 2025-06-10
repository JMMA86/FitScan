package icesi.edu.co.fitscan.features.mealPlan.data.usecases

import icesi.edu.co.fitscan.features.mealPlan.data.dto.*
import icesi.edu.co.fitscan.features.mealPlan.data.repositories.MealPlanRepository

class CreateMealPlanUseCase(private val repository: MealPlanRepository) {
    suspend fun invoke(
        customerId: String,
        name: String,
        description: String?,
        goal: String?,
        mealIds: List<String>,
        restrictionIds: List<String>,
        preferenceIds: List<String>
    ): Boolean {
        val mealPlan = MealPlanDto(
            customer_id = customerId,
            name = name,
            description = description,
            goal = goal
        )
        val created = repository.createMealPlan(mealPlan)
        if (created?.id != null) {
            mealIds.forEach { mealId ->
                repository.addMealToMealPlan(MealPlanMealDto(meal_plan_id = created.id, meal_id = mealId))
            }
            restrictionIds.forEach { restrictionId ->
                repository.addRestrictionToMealPlan(created.id, restrictionId)
            }
            preferenceIds.forEach { preferenceId ->
                repository.addPreferenceToMealPlan(created.id, preferenceId)
            }
            return true
        }
        return false
    }
}
