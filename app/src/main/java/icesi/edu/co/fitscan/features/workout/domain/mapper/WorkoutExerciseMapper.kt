package icesi.edu.co.fitscan.features.workout.domain.mapper

import icesi.edu.co.fitscan.features.workout.data.dto.WorkoutExerciseDto
import icesi.edu.co.fitscan.features.workout.domain.model.WorkoutExercise
import java.util.UUID

class WorkoutExerciseMapper {

    fun toDomain(dto: WorkoutExerciseDto): WorkoutExercise {
        return WorkoutExercise(
            id = UUID.fromString(dto.id),
            workoutId = UUID.fromString(dto.workoutId),
            exerciseId = UUID.fromString(dto.exerciseId),
            sets = dto.sets,
            reps = dto.reps,
            isAiSuggested = dto.isAiSuggested
        )
    }

    fun toDto(workoutExercise: WorkoutExercise): WorkoutExerciseDto {
        return WorkoutExerciseDto(
            id = workoutExercise.id.toString(),
            workoutId = workoutExercise.workoutId.toString(),
            exerciseId = workoutExercise.exerciseId.toString(),
            sets = workoutExercise.sets,
            reps = workoutExercise.reps,
            isAiSuggested = workoutExercise.isAiSuggested
        )
    }

}
