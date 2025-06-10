package icesi.edu.co.fitscan.features.mealPlan.data.dto

import icesi.edu.co.fitscan.domain.model.Meal

// DTO para MealPlan, puede mapearse desde/hacia el modelo de dominio

data class MealPlanDto(
    val id: String = "",
    val title: String,
    val description: String,
    val author: String,
    val tags: List<String> = emptyList(),
    val meals: List<Meal> = emptyList(),
    val caloriesGoal: Int = 0,
    val protein: Int = 0,
    val carbs: Int = 0,
    val fats: Int = 0,
    val progress: Int = 0
) 