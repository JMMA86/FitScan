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
    suspend fun getMealPlans(): List<MealPlanDto> {
        return dataSource.getMealPlans().body()?.data ?: emptyList()
    }
    suspend fun createMealPlan(mealPlan: MealPlanDto): MealPlanDto? {
        val response = dataSource.createMealPlan(mealPlan)
        // Considera éxito si el status HTTP es exitoso, aunque el body sea null
        return if (response.isSuccessful) response.body() ?: MealPlanDto(
            id = null, // o puedes intentar extraer el id de otra forma si tu backend lo retorna en headers
            customer_id = mealPlan.customer_id,
            name = mealPlan.name,
            description = mealPlan.description,
            goal = mealPlan.goal
        ) else null
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
