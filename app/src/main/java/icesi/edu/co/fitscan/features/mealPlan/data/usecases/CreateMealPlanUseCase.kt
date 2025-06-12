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
        // Considera éxito si el objeto retornado no es null (aunque el id sea null)
        if (created != null) {
            created.id?.let { id ->
                mealIds.forEach { mealId ->
                    repository.addMealToMealPlan(MealPlanMealDto(meal_plan_id = id, meal_id = mealId))
                }
                restrictionIds.forEach { restrictionId ->
                    repository.addRestrictionToMealPlan(id, restrictionId)
                }
                preferenceIds.forEach { preferenceId ->
                    repository.addPreferenceToMealPlan(id, preferenceId)
                }
            }
            return true
        }
        return false
    }
}
