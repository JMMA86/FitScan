package icesi.edu.co.fitscan.features.workout.data.api

import icesi.edu.co.fitscan.features.workout.data.dto.WorkoutDto
import icesi.edu.co.fitscan.features.workout.data.dto.WorkoutExerciseDto
import retrofit2.Response
import retrofit2.http.*

@JvmSuppressWildcards
interface WorkoutApiService {

    companion object {
        const val BASE_PATH = "items"
    }

    @GET("$BASE_PATH/workout")
    suspend fun getAllWorkouts(): Response<List<WorkoutDto>>

    @GET("$BASE_PATH/workout/{id}")
    suspend fun getWorkoutById(
        @Path("id") id: String
    ): Response<WorkoutDto>

    @POST("$BASE_PATH/workout")
    suspend fun createWorkout(
        @Body workout: WorkoutDto
    ): Response<WorkoutDto>

    @PUT("$BASE_PATH/workout/{id}")
    suspend fun updateWorkout(
        @Path("id") id: String,
        @Body workout: WorkoutDto
    ): Response<WorkoutDto>

    @DELETE("$BASE_PATH/workout/{id}")
    suspend fun deleteWorkout(
        @Path("id") id: String
    ): Response<Unit>

    @GET("$BASE_PATH/workout/customer/{customerId}")
    suspend fun getWorkoutsByCustomerId(
        @Path("customerId") customerId: String
    ): Response<List<WorkoutDto>>

    @POST("$BASE_PATH/workout/{workoutId}/exercise")
    suspend fun addExerciseToWorkout(
        @Path("workoutId") workoutId: String,
        @Body workoutExercise: WorkoutExerciseDto
    ): Response<WorkoutExerciseDto>

    @DELETE("$BASE_PATH/workout/{workoutId}/exercise/{exerciseId}")
    suspend fun removeExerciseFromWorkout(
        @Path("workoutId") workoutId: String,
        @Path("exerciseId") exerciseId: String
    ): Response<Unit>

    @PUT("$BASE_PATH/workout/{workoutId}/exercise/{exerciseId}")
    suspend fun updateWorkoutExercise(
        @Path("workoutId") workoutId: String,
        @Path("exerciseId") exerciseId: String,
        @Body workoutExercise: WorkoutExerciseDto
    ): Response<WorkoutExerciseDto>
} 