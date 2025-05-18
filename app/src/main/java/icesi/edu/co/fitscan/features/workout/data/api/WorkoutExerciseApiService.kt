package icesi.edu.co.fitscan.features.workout.data.api

import icesi.edu.co.fitscan.features.workout.data.dto.WorkoutExerciseDto
import retrofit2.Response
import retrofit2.http.*

@JvmSuppressWildcards
interface WorkoutExerciseApiService {

    companion object {
        const val BASE_PATH = "items"
    }

    @GET("$BASE_PATH/workout/{workoutId}/exercise")
    suspend fun getExercisesByWorkoutId(
        @Path("workoutId") workoutId: String
    ): Response<List<WorkoutExerciseDto>>

    @GET("$BASE_PATH/workout/{workoutId}/exercise/{exerciseId}")
    suspend fun getWorkoutExercisesById(
        @Path("workoutId") workoutId: String,
        @Path("exerciseId") exerciseId: String
    ): Response<WorkoutExerciseDto>

    @POST("$BASE_PATH/workout/{workoutId}/exercise/{exerciseId}")
    suspend fun addExerciseToWorkout(
        @Path("workoutId") workoutId: String,
        @Path("exerciseId") exerciseId: String,
    ): Response<WorkoutExerciseDto>

    @PUT("$BASE_PATH/workout/{workoutId}/exercise/{exerciseId}")
    suspend fun updateWorkoutExercise(
        @Path("workoutId") workoutId: String,
        @Path("exerciseId") exerciseId: String,
        @Body workoutExercise: WorkoutExerciseDto
    ): Response<WorkoutExerciseDto>

    @DELETE("$BASE_PATH/workout/{workoutId}/exercise/{exerciseId}")
    suspend fun removeExerciseFromWorkout(
        @Path("workoutId") workoutId: String,
        @Path("exerciseId") exerciseId: String
    ): Response<Unit>

    @PUT("$BASE_PATH/workout/{workoutId}/exercise/reorder")
    suspend fun reorderWorkoutExercise(
        @Path("workoutId") workoutId: String,
        @Body exerciseOrder: List<String>
    ): Response<List<WorkoutExerciseDto>>

    @PATCH("$BASE_PATH/workout/{workoutId}/exercise/{exerciseId}/complete")
    suspend fun markExerciseAsCompleted(
        @Path("workoutId") workoutId: String,
        @Path("exerciseId") exerciseId: String
    ): Response<WorkoutExerciseDto>
} 