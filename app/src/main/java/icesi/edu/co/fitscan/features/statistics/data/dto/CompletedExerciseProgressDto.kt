package icesi.edu.co.fitscan.features.statistics.data.dto

import icesi.edu.co.fitscan.features.workout.data.dto.WorkoutSessionDto
import icesi.edu.co.fitscan.features.workout.data.dto.ExerciseDto

data class CompletedExerciseProgressDto(
    val id: String?,
    val workout_session_id: WorkoutSessionDto?,
    val exercise_id: ExerciseDto?, // Can be either ExerciseDto object or string ID
    val sets: Int?,
    val reps: Int?,
    val rpe: Int?,
    val weight_kg: Int?
)