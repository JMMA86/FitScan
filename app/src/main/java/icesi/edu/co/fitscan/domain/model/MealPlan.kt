package icesi.edu.co.fitscan.domain.model

import java.io.Serializable

// Modelo principal para un plan alimenticio

data class MealPlan(
    val id: String = "",
    val title: String,
    val description: String,
    val author: String,
    val tags: List<String> = emptyList(),
    val meals: List<Meal> = emptyList(),
    val caloriesGoal: Int = 0,
    val protein: Int = 0, // gramos
    val carbs: Int = 0,   // gramos
    val fats: Int = 0,    // gramos
    val progress: Int = 0 // calorías consumidas hoy
) : Serializable 