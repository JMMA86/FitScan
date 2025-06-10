package icesi.edu.co.fitscan.features.mealPlan.ui.model

data class UiMealPlan(
    val name: String = "",
    val description: String = "",
    val goal: String = "",
    val selectedMealIds: List<String> = emptyList(),
    val selectedRestrictions: List<String> = emptyList(),
    val selectedPreferences: List<String> = emptyList()
)
