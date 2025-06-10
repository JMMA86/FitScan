package icesi.edu.co.fitscan.features.nutrition.data.repositories

import icesi.edu.co.fitscan.domain.model.MealPlan

interface IMealPlanRepository {
    suspend fun getMealPlans(): List<MealPlan>
    suspend fun getMealPlanById(id: String): MealPlan?
    suspend fun createMealPlan(mealPlan: MealPlan): Boolean
    suspend fun updateMealPlan(mealPlan: MealPlan): Boolean
    suspend fun deleteMealPlan(id: String): Boolean
} 