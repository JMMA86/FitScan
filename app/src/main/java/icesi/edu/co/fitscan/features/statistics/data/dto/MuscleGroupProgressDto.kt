package icesi.edu.co.fitscan.features.statistics.data.dto

import com.google.gson.annotations.SerializedName

data class MuscleGroupProgressDto(
    @SerializedName("muscle_group")
    val muscleGroup: MuscleGroupDto,
    @SerializedName("progress_percentage")
    val progressPercentage: Float,
    @SerializedName("total_exercises")
    val totalExercises: Int,
    @SerializedName("completed_this_week")
    val completedThisWeek: Int,
    @SerializedName("avg_weight_lifted")
    val avgWeightLifted: Float? = 0f,
    @SerializedName("total_volume")
    val totalVolume: Float? = 0f
)
