package icesi.edu.co.fitscan.features.workout.domain.mapper

import icesi.edu.co.fitscan.features.workout.data.dto.WorkoutExerciseDto
import icesi.edu.co.fitscan.features.workout.domain.model.WorkoutExercise
import java.util.UUID

class WorkoutExerciseMapper {

    fun toDomain(dto: WorkoutExerciseDto): WorkoutExercise {
        return WorkoutExercise(
            id = UUID.fromString(dto.id),
            workoutId = UUID.fromString(dto.workout_id),
            exerciseId = UUID.fromString(dto.exercise_id),
            sets = dto.sets,
            reps = dto.reps,
            isAiSuggested = dto.isAiSuggested
        )
    }

    fun toDto(workoutExercise: WorkoutExercise): WorkoutExerciseDto {
        return WorkoutExerciseDto(
            id = workoutExercise.id.toString(),
            workout_id = workoutExercise.workoutId.toString(),
            exercise_id = workoutExercise.exerciseId.toString(),
            sets = workoutExercise.sets,
            reps = workoutExercise.reps,
            isAiSuggested = workoutExercise.isAiSuggested
        )
    }

}
