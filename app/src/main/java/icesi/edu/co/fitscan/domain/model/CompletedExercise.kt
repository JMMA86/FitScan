package icesi.edu.co.fitscan.domain.model

data class CompletedExercise(
    val id: String?,
    val workoutSessionId: String?,
    val exerciseId: String?,
    val sets: Int?,
    val reps: Int?,
    val rpe: Int?,
    val weightKg: Int?,
)