package icesi.edu.co.fitscan.features.statistics.data.mapper

import icesi.edu.co.fitscan.domain.model.MuscleGroupProgress
import icesi.edu.co.fitscan.features.statistics.data.dto.MuscleGroupProgressDto

object MuscleGroupProgressMapper {
    fun toDomain(dto: MuscleGroupProgressDto): MuscleGroupProgress = MuscleGroupProgress(
        muscleGroup = MuscleGroupMapper.toDomain(dto.muscleGroup),
        progressPercentage = dto.progressPercentage,
        totalExercises = dto.totalExercises,
        completedThisWeek = dto.completedThisWeek,
        avgWeightLifted = dto.avgWeightLifted ?: 0f,
        totalVolume = dto.totalVolume ?: 0f
    )
}
