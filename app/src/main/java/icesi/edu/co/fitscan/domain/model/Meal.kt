package icesi.edu.co.fitscan.domain.model

import java.io.Serializable

// Modelo para una comida dentro de un plan alimenticio

data class Meal(
    val name: String,
    val description: String = "",
    val foods: List<String> = emptyList(),
    val type: MealType = MealType.OTHER
) : Serializable

enum class MealType {
    BREAKFAST, SNACK, LUNCH, DINNER, OTHER
} 