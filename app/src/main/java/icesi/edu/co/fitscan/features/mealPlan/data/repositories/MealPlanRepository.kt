package icesi.edu.co.fitscan.features.mealPlan.data.repositories

import icesi.edu.co.fitscan.features.mealPlan.data.datasources.IMealPlanDataSource
import icesi.edu.co.fitscan.features.mealPlan.data.dto.*

class MealPlanRepository(private val dataSource: IMealPlanDataSource) {
    suspend fun getGoals(): List<FitnessGoalDto> {
        return dataSource.getGoals().body()?.data ?: emptyList()
    }
    suspend fun getDietaryRestrictions(): List<DietaryRestrictionDto> {
        return dataSource.getDietaryRestrictions().body()?.data ?: emptyList()
    }
    suspend fun getDietaryPreferences(): List<DietaryPreferenceDto> {
        return dataSource.getDietaryPreferences().body()?.data ?: emptyList()
    }
    suspend fun getMeals(): List<MealDto> {
        return dataSource.getMeals().body()?.data ?: emptyList()
    }
    suspend fun createMealPlan(mealPlan: MealPlanDto): MealPlanDto? {
        return dataSource.createMealPlan(mealPlan).body()
    }
    suspend fun addMealToMealPlan(mealPlanMeal: MealPlanMealDto): MealPlanMealDto? {
        return dataSource.addMealToMealPlan(mealPlanMeal).body()
    }
    suspend fun addRestrictionToMealPlan(mealPlanId: String, restrictionId: String) {
        dataSource.addRestrictionToMealPlan(
            MealPlanRestrictionDto(meal_plan_id = mealPlanId, restriction_id = restrictionId)
        )
    }
    suspend fun addPreferenceToMealPlan(mealPlanId: String, preferenceId: String) {
        dataSource.addPreferenceToMealPlan(
            MealPlanPreferenceDto(meal_plan_id = mealPlanId, preference_id = preferenceId)
        )
    }
}
