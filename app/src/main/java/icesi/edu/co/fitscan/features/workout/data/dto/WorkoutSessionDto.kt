package icesi.edu.co.fitscan.features.workout.data.dto

data class WorkoutSessionDto(
    val id: String?,
    val customer_id: String,
    val workout_id: String,
    val start_time: String,
    val end_time: String,
    val calories_burned: Int,
    val distance_km: Float?,
    val average_heart_rate: Int
)
