package icesi.edu.co.fitscan.features.workout.data.dto

import com.google.gson.annotations.SerializedName
import icesi.edu.co.fitscan.features.statistics.data.dto.MuscleGroupDto

data class ExerciseDto(
    val id: String,
    val name: String,
    val description: String?,
    @SerializedName("muscle_groups")
    val muscleGroups: String?, // Keep for backward compatibility
    @SerializedName("primary_muscle_group_id")
    val primaryMuscleGroupId: MuscleGroupDto? = null, // Changed to full object
    @SerializedName("secondary_muscle_groups")
    val secondaryMuscleGroups: List<WorkoutSecondaryMuscleGroupDto>? = null
)

data class WorkoutSecondaryMuscleGroupDto(
    @SerializedName("muscle_group")
    val muscleGroup: MuscleGroupDto
)

