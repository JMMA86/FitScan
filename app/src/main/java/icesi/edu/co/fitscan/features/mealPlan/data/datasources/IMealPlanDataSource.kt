package icesi.edu.co.fitscan.features.mealPlan.data.datasources

import icesi.edu.co.fitscan.features.mealPlan.data.dto.*
import retrofit2.Response
import retrofit2.http.*

interface IMealPlanDataSource {
    @GET("items/fitness_goal")
    suspend fun getGoals(): Response<GoalsResponse>

    @GET("items/dietary_restriction")
    suspend fun getDietaryRestrictions(): Response<DietaryRestrictionsResponse>

    @GET("items/dietary_preference")
    suspend fun getDietaryPreferences(): Response<DietaryPreferencesResponse>

    @GET("items/meal")
    suspend fun getMeals(): Response<MealsResponse>

    @GET("items/meal_plan")
    suspend fun getMealPlans(): Response<MealPlansResponse>

    @POST("items/meal_plan")
    suspend fun createMealPlan(@Body mealPlan: MealPlanDto): Response<MealPlanDto>

    @POST("items/meal_plan_meal")
    suspend fun addMealToMealPlan(@Body mealPlanMeal: MealPlanMealDto): Response<MealPlanMealDto>

    @POST("items/meal_plan_restriction")
    suspend fun addRestrictionToMealPlan(@Body mealPlanRestriction: MealPlanRestrictionDto): Response<MealPlanRestrictionDto>

    @POST("items/meal_plan_preference")
    suspend fun addPreferenceToMealPlan(@Body mealPlanPreference: MealPlanPreferenceDto): Response<MealPlanPreferenceDto>
}

data class GoalsResponse(val data: List<FitnessGoalDto>)
data class DietaryRestrictionsResponse(val data: List<DietaryRestrictionDto>)
data class DietaryPreferencesResponse(val data: List<DietaryPreferenceDto>)
data class MealsResponse(val data: List<MealDto>)
data class MealPlansResponse(val data: List<MealPlanDto>)
