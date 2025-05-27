package icesi.edu.co.fitscan.domain.model

data class WorkoutSession(
    // Enserio cristian? No UUIDs?
    val id: String?,
    val customerId: String,
    val workoutId: String,
    val startTime: String,
    val endTime: String,
    val caloriesBurned: Int,
    val distanceKm: Float?,
    val averageHeartRate: Int
)
