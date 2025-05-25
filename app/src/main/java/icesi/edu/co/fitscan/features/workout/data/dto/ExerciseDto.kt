package icesi.edu.co.fitscan.features.workout.data.dto

import com.google.gson.annotations.SerializedName

data class ExerciseDto(
    val id: String,
    val name: String,
    val description: String?,
    @SerializedName("muscle_groups")
    val muscleGroups: String?
)

