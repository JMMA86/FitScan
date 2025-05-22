package icesi.edu.co.fitscan.features.workout.data.dataSources

import icesi.edu.co.fitscan.features.workout.data.dto.WorkoutDto
import retrofit2.Response
import retrofit2.http.*

@JvmSuppressWildcards
interface IWorkoutDataSource {

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

    @GET("$BASE_PATH/workout/customer/{customer_id}")
    suspend fun getWorkoutsByCustomerId(
        @Path("customer_id") customerId: String
    ): Response<List<WorkoutDto>>
} 