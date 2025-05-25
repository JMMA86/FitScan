package icesi.edu.co.fitscan.features.workout.data.dto

data class WorkoutsResponse(
    val data: List<WorkoutDto>
)

data class WorkoutDto(
    val id: String,
    val customer_id: String,
    val name: String,
    val type: String,
    val durationMinutes: Int?,
    val difficulty: String?,
    val dateCreated: String
)
