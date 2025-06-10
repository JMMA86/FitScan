package icesi.edu.co.fitscan.features.mealPlan.data.dto

data class MealPlanDto(
    val id: String? = null,
    val customer_id: String,
    val name: String,
    val description: String?,
    val goal: String?,
    val date_created: String? = null
)

data class MealDto(
    val id: String? = null,
    val meal_type: String?,
    val name: String,
    val description: String?,
    val calories: Int?,
    val protein_g: Int?,
    val carbs_g: Int?,
    val fat_g: Int?
)

data class MealPlanMealDto(
    val id: String? = null,
    val meal_plan_id: String,
    val meal_id: String
)

data class DietaryRestrictionDto(
    val id: String,
    val name: String
)

data class DietaryPreferenceDto(
    val id: String,
    val name: String
)

data class FitnessGoalDto(
    val id: String,
    val goal_name: String,
    val description: String?
)

data class MealPlanRestrictionDto(
    val id: String? = null,
    val meal_plan_id: String,
    val restriction_id: String
)

data class MealPlanPreferenceDto(
    val id: String? = null,
    val meal_plan_id: String,
    val preference_id: String
)
