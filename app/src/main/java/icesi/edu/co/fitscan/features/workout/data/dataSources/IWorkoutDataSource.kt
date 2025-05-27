package icesi.edu.co.fitscan.features.workout.data.dataSources

import icesi.edu.co.fitscan.features.workout.data.dto.ApiResponseDTO
import icesi.edu.co.fitscan.features.workout.data.dto.WorkoutDto
import icesi.edu.co.fitscan.features.workout.data.dto.WorkoutsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

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
    ): Response<ApiResponseDTO<WorkoutDto>>

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

    @GET("$BASE_PATH/workout/")
    suspend fun getWorkoutsByCustomerId(
        @Query("filter[customer_id][_eq]") customerId: String
    ): Response<WorkoutsResponse>
} 