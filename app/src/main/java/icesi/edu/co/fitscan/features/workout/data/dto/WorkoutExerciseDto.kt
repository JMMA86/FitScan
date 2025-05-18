package icesi.edu.co.fitscan.features.workout.data.dto

data class WorkoutExerciseDto(
    val id: String,
    val workoutId: String,
    val exerciseId: String,
    val sets: Int,
    val reps: Int,
    val isAiSuggested: Boolean
)
