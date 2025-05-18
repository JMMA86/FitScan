package icesi.edu.co.fitscan.features.workout.data.dto

data class WorkoutDto(
    val id: String,
    val customerId: String,
    val name: String,
    val type: String,
    val durationMinutes: Int?,
    val difficulty: String?,
    val dateCreated: String
)
