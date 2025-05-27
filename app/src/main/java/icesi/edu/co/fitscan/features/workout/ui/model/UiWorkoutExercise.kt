package icesi.edu.co.fitscan.features.workout.ui.model

/**
 * UI model for displaying a workout exercise with its name, sets, and reps.
 */
data class UiWorkoutExercise(
    val id: String, // UUID as string
    val name: String,
    val sets: Int,
    val reps: Int
)
