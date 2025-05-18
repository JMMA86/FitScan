package icesi.edu.co.fitscan.features.statistics.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface ExerciseStatisticsRemoteDataSource {
    @GET("/items/workout_session")
    suspend fun getWorkoutSessions(): WorkoutSessionResponse
}

data class WorkoutSessionResponse(
    val data: List<WorkoutSession>
)

data class WorkoutSession(
    val id: String,
    val customer_id: String,
    val workout_id: String,
    val start_time: String,
    val end_time: String,
    val calories_burned: Int,
    val distance_km: Float?,
    val average_heart_rate: Int
)
