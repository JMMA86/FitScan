package icesi.edu.co.fitscan.features.workout.data.mapper

import icesi.edu.co.fitscan.domain.model.CompletedExercise
import icesi.edu.co.fitscan.features.workout.data.dto.CompletedExerciseDTO

class CompletedExerciseMapper {
    fun fromDto(dto: CompletedExerciseDTO): CompletedExercise = CompletedExercise(
        id = dto.id,
        workoutSessionId = dto.workout_session_id,
        exerciseId = dto.exercise_id,
        sets = dto.sets,
        reps = dto.reps,
        rpe = dto.rpe,
        weightKg = dto.weight_kg
    )

    fun toDto(domain: CompletedExercise): CompletedExerciseDTO = CompletedExerciseDTO(
        id = domain.id,
        workout_session_id = domain.workoutSessionId ?: "",
        exercise_id = domain.exerciseId ?: "",
        sets = domain.sets,
        reps = domain.reps,
        rpe = domain.rpe,
        weight_kg = domain.weightKg ?: 0 // Default to 0 if null
    )
}