package icesi.edu.co.fitscan.features.statistics.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Header
import icesi.edu.co.fitscan.features.statistics.data.remote.dto.ExerciseItem
import icesi.edu.co.fitscan.features.statistics.data.remote.dto.ExerciseListResponse
import icesi.edu.co.fitscan.features.statistics.data.remote.dto.CompletedExerciseProgressDto

interface ExerciseStatisticsRemoteDataSource {
    @GET("/items/workout_session")
    suspend fun getWorkoutSessions(
        @Header("Authorization") token: String,
        @Query("filter[customer_id][_eq]") customerId: String
    ): WorkoutSessionResponse

    @GET("/items/progress_photo")
    suspend fun getProgressPhotos(
        @Header("Authorization") token: String,
        @Query("filter[customer_id][_eq]") customerId: String
    ): ProgressPhotoResponse

    @GET("/items/workout_session")
    suspend fun getDistanceStats(
        @Header("Authorization") token: String,
        @Query("filter[customer_id][_eq]") customerId: String
    ): DistanceStatsResponse

    @GET("/items/body_measure")
    suspend fun getWeightStats(
        @Header("Authorization") token: String,
        @Query("filter[id][_eq]") bodyMeasureId: String
    ): WeightStatsResponse

    @GET("/items/workout_session")
    suspend fun getCaloriesStats(
        @Header("Authorization") token: String,
        @Query("filter[customer_id][_eq]") customerId: String
    ): CaloriesStatsResponse

    @GET("/items/completed_exercise")
    suspend fun getWeightMovedStats(
        @Header("Authorization") token: String,
        @Query("filter[workout_session_id][_in]") sessionIds: String
    ): WeightMovedStatsResponse

    @GET("/items/customer")
    suspend fun getCustomer(
        @Header("Authorization") token: String,
        @Query("filter[id][_eq]") customerId: String
    ): CustomerResponse

    @GET("/items/completed_exercise")
    suspend fun getCompletedExercisesForProgress(
        @Header("Authorization") token: String,
        @Query("filter[workout_session_id][customer_id][_eq]") customerId: String,
        @Query("filter[exercise_id][_eq]") exerciseId: String,
        @Query("filter[workout_session_id][start_time][_gte]") fromDate: String?,
        @Query("filter[workout_session_id][start_time][_lte]") toDate: String?,
        @Query("fields") fields: String = "id,workout_session_id,exercise_id,sets,reps,rpe,weight_kg,workout_session_id.start_time"
    ): CompletedExerciseProgressResponse

    @GET("/items/exercise")
    suspend fun getAllExercises(
        @Header("Authorization") token: String
    ): ExerciseListResponse
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

data class ProgressPhotoResponse(val data: List<ProgressPhoto>)
data class ProgressPhoto(
    val id: String,
    val customer_id: String,
    val photo_date: String?,
    val title: String?,
    val image_path: String?
)

data class DistanceStatsResponse(val data: List<WorkoutSession>)

data class WeightStatsResponse(val data: List<BodyMeasure>)
data class BodyMeasure(
    val id: String,
    val weight_kg: Int?
)

data class CaloriesStatsResponse(val data: List<WorkoutSession>)

data class WeightMovedStatsResponse(val data: List<CompletedExercise>)
data class CompletedExercise(
    val id: String,
    val workout_session_id: String,
    val exercise_id: String,
    val sets: Int?,
    val reps: Int?,
    val rpe: Int?,
    val weight_kg: Int?
)

data class CustomerResponse(val data: List<Customer>)
data class Customer(
    val id: String,
    val body_measure_id: String?
)

data class CompletedExerciseProgressResponse(val data: List<CompletedExerciseProgressDto>)
