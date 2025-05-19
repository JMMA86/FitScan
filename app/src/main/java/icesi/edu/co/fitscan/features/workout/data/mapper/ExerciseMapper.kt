package icesi.edu.co.fitscan.features.workout.data.mapper

import icesi.edu.co.fitscan.features.workout.data.dto.ExerciseDto
import icesi.edu.co.fitscan.features.workout.domain.model.Exercise
import java.util.UUID

class ExerciseMapper {

    fun toDomain(dto: ExerciseDto): Exercise {
        return Exercise(
            id = UUID.fromString(dto.id),
            name = dto.name,
            description = dto.description,
            muscleGroups = dto.muscleGroups
        )
    }

    fun toDto(exercise: Exercise): ExerciseDto {
        return ExerciseDto(
            id = exercise.id.toString(),
            name = exercise.name,
            description = exercise.description,
            muscleGroups = exercise.muscleGroups
        )
    }

}
