package icesi.edu.co.fitscan.features.statistics.data.remote

import icesi.edu.co.fitscan.features.statistics.data.dto.CaloriesStatsResponseDto
import icesi.edu.co.fitscan.features.statistics.data.dto.CompletedExerciseProgressDto
import icesi.edu.co.fitscan.features.statistics.data.dto.CompletedExerciseProgressResponseDto
import icesi.edu.co.fitscan.features.statistics.data.dto.ExerciseListResponseDto
import icesi.edu.co.fitscan.features.statistics.data.dto.FileUpdateRequest
import icesi.edu.co.fitscan.features.statistics.data.dto.FileUploadResponse
import icesi.edu.co.fitscan.features.statistics.data.dto.ProgressPhotoCreateRequest
import icesi.edu.co.fitscan.features.statistics.data.dto.ProgressPhotoResponseDto
import icesi.edu.co.fitscan.features.statistics.data.dto.ProgressPhotoUpdateRequest
import icesi.edu.co.fitscan.features.statistics.data.dto.WeightMovedStatsResponseDto
import icesi.edu.co.fitscan.features.workout.data.dto.WorkoutSessionResponseDto
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface StatisticsRemoteDataSource {
    @GET("/items/progress_photo")
    suspend fun getProgressPhotos(
        @Query("filter[customer_id][_eq]") customerId: String
    ): ProgressPhotoResponseDto

    @GET("/items/workout_session")
    suspend fun getWorkoutSessions(
        @Query("filter[customer_id][_eq]") customerId: String
    ): WorkoutSessionResponseDto

    @GET("/items/completed_exercise")
    suspend fun getCompletedExercisesForProgress(
        @Query("filter[workout_session_id][customer_id][_eq]") customerId: String,
        @Query("filter[exercise_id][_eq]") exerciseId: String,
        @Query("filter[workout_session_id][start_time][_gte]") fromDate: String?,
        @Query("filter[workout_session_id][start_time][_lte]") toDate: String?,
        @Query("fields") fields: String = "id,workout_session_id,exercise_id,sets,reps,rpe,weight_kg,workout_session_id.start_time"
    ): CompletedExerciseProgressResponseDto

    @GET("/items/exercise")
    suspend fun getAllExercises(): ExerciseListResponseDto

    @Multipart
    @POST("/files")
    suspend fun uploadFile(
        @retrofit2.http.Header("Authorization") token: String,
        @Part file: MultipartBody.Part
    ): FileUploadResponse

    @POST("/items/progress_photo")
    suspend fun createProgressPhoto(
        @Body request: ProgressPhotoCreateRequest
    ): Response<Unit>

    @PATCH("/items/progress_photo/{id}")
    suspend fun updateProgressPhoto(
        @Path("id") photoId: String,
        @Body request: ProgressPhotoUpdateRequest
    ): Response<Unit>

    @PATCH("/files/{id}")
    suspend fun makeFilePublic(
        @Path("id") fileId: String,
        @Body body: FileUpdateRequest
    ): Response<Unit>

    @GET("/items/workout_session")
    suspend fun getCaloriesStats(
        @Query("filter[customer_id][_eq]") customerId: String
    ): CaloriesStatsResponseDto

    @GET("/items/completed_exercise")
    suspend fun getWeightMovedStats(
        @Query("filter[workout_session_id][_in]") sessionIds: String,
        @Query("fields") fields: String = "workout_session_id.*,*"
    ): WeightMovedStatsResponseDto

    @POST("/items/completed_exercise")
    suspend fun createCompletedExercise(
        @Body completedExercise: CompletedExerciseProgressDto
    ): Response<CompletedExerciseProgressDto>
}
