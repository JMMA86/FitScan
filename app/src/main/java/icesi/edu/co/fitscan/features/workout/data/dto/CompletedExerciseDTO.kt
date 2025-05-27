package icesi.edu.co.fitscan.features.workout.data.dto

// TODO: Erase the ones that are placed in statistics module
data class CompletedExerciseDTO(
    val id: String?,
    val workout_session_id: String,
    val exercise_id: String,
    val sets: Int?,
    val reps: Int?,
    val rpe: Int?,
    val weight_kg: Int? = null // Optional, not always present
)