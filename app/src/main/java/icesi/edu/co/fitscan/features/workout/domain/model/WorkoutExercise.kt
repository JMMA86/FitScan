package icesi.edu.co.fitscan.features.workout.domain.model

import java.util.UUID

data class WorkoutExercise(
    val id: UUID = UUID.randomUUID(),
    val workoutId: UUID,
    val exerciseId: UUID,
    val sets: Int,
    val reps: Int,
    val isAiSuggested: Boolean = false,
) 