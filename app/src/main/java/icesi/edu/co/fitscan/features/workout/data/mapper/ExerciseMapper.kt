package icesi.edu.co.fitscan.features.workout.data.mapper

import icesi.edu.co.fitscan.features.workout.data.dto.ExerciseDto
import icesi.edu.co.fitscan.features.workout.data.dto.WorkoutSecondaryMuscleGroupDto
import icesi.edu.co.fitscan.domain.model.Exercise
import icesi.edu.co.fitscan.features.statistics.data.dto.MuscleGroupDto
import icesi.edu.co.fitscan.features.statistics.data.mapper.MuscleGroupMapper
import java.util.UUID

class ExerciseMapper {

    private val muscleGroupMapper = MuscleGroupMapper

    fun toDomain(dto: ExerciseDto): Exercise {
        val safeDescription = dto.description ?: ""
        val safeMuscleGroups = dto.muscleGroups ?: ""
          // Map primary muscle group
        val primaryMuscleGroup = dto.primaryMuscleGroupId?.let { muscleGroupDto ->
            muscleGroupMapper.toDomain(muscleGroupDto)
        }
          // Map secondary muscle groups
        val secondaryMuscleGroups = dto.secondaryMuscleGroups?.map { secondaryDto ->
            muscleGroupMapper.toDomain(secondaryDto.muscleGroup)
        } ?: emptyList()
        
        android.util.Log.d("ExerciseMapper", "[toDomain] RAW dto: id=${dto.id}, name=${dto.name}, description='${dto.description}', muscleGroups='${dto.muscleGroups}'")
        android.util.Log.d("ExerciseMapper", "[toDomain] SAFE: id=${dto.id}, name=${dto.name}, description='${safeDescription}', muscleGroups='${safeMuscleGroups}'")
        
        return Exercise(
            id = UUID.fromString(dto.id),
            name = dto.name,
            description = safeDescription,
            muscleGroups = safeMuscleGroups, // Keep for backward compatibility
            primaryMuscleGroup = primaryMuscleGroup,
            secondaryMuscleGroups = secondaryMuscleGroups
        )
    }    fun toDto(exercise: Exercise): ExerciseDto {
        val secondaryMuscleGroupDtos = exercise.secondaryMuscleGroups.map { muscleGroup ->
            WorkoutSecondaryMuscleGroupDto(
                muscleGroup = muscleGroupMapper.toDto(muscleGroup)
            )
        }
          return ExerciseDto(
            id = exercise.id.toString(),
            name = exercise.name,
            description = if (exercise.description.isBlank()) null else exercise.description,
            muscleGroups = if (exercise.muscleGroups.isBlank()) null else exercise.muscleGroups, // Keep for backward compatibility
            primaryMuscleGroupId = exercise.primaryMuscleGroup?.let { muscleGroupMapper.toDto(it) },
            secondaryMuscleGroups = if (secondaryMuscleGroupDtos.isNotEmpty()) secondaryMuscleGroupDtos else null
        )
    }

}
