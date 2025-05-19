package icesi.edu.co.fitscan.features.statistics.data.remote.dto

/**
 * DTO for completed exercise progress, including the related workout session's start_time for direct aggregation.
 */
data class CompletedExerciseProgressDto(
    val id: String?,
    val exercise_id: String?,
    val workout_session_id: WorkoutSessionId?,
    val sets: Int?,
    val reps: Int?,
    val weight_kg: Int?
) {
    data class WorkoutSessionId(
        val start_time: String?
    )
}
