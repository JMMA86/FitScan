package icesi.edu.co.fitscan.features.statistics.data.remote

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Body
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path
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

    @Multipart
    @POST("/files")
    suspend fun uploadFile(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part
    ): FileUploadResponse

    @POST("/items/progress_photo")
    suspend fun createProgressPhoto(
        @Header("Authorization") token: String,
        @Body request: ProgressPhotoCreateRequest
    ): Response<Unit>

    @PATCH("/files/{id}")
    suspend fun makeFilePublic(
        @Header("Authorization") token: String,
        @Path("id") fileId: String,
        @Body body: FileUpdateRequest
    ): Response<Unit>
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

data class FileUploadResponse(val data: FileUploadData)
data class FileUploadData(val id: String)

data class ProgressPhotoCreateRequest(
    val customer_id: String,
    val image_path: String,
    val title: String? = null
)

data class FileUpdateRequest(
    val title: String,
    val filename_download: String,
    val public: Boolean = true
)
