package icesi.edu.co.fitscan.features.workout.data.dto

data class WorkoutExerciseDto(
    val id: String,
    val workout_id: String,
    val exercise_id: String,
    val sets: Int,
    val reps: Int,
    val isAiSuggested: Boolean
)
