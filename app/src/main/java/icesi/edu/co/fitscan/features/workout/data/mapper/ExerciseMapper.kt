package icesi.edu.co.fitscan.features.workout.data.mapper

import icesi.edu.co.fitscan.features.workout.data.dto.ExerciseDto
import icesi.edu.co.fitscan.domain.model.Exercise
import java.util.UUID

class ExerciseMapper {

    fun toDomain(dto: ExerciseDto): Exercise {
        val safeDescription = dto.description ?: ""
        val safeMuscleGroups = dto.muscleGroups ?: ""
        android.util.Log.d("ExerciseMapper", "[toDomain] RAW dto: id=${dto.id}, name=${dto.name}, description='${dto.description}', muscleGroups='${dto.muscleGroups}'")
        android.util.Log.d("ExerciseMapper", "[toDomain] SAFE: id=${dto.id}, name=${dto.name}, description='${safeDescription}', muscleGroups='${safeMuscleGroups}'")
        return Exercise(
            id = UUID.fromString(dto.id),
            name = dto.name,
            description = safeDescription,
            muscleGroups = safeMuscleGroups
        )
    }

    fun toDto(exercise: Exercise): ExerciseDto {
        return ExerciseDto(
            id = exercise.id.toString(),
            name = exercise.name,
            description = if (exercise.description.isBlank()) null else exercise.description,
            muscleGroups = if (exercise.muscleGroups.isBlank()) null else exercise.muscleGroups
        )
    }

}
