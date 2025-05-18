package icesi.edu.co.fitscan.features.workout.domain.repository

import icesi.edu.co.fitscan.features.workout.domain.data.remote.response.WorkoutResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WorkoutListRepository {
    @GET("items/workout")
    suspend fun getWorkouts(
        @Header("Authorization") token: String,
        @Query("filter[customer_id][_eq]") customerId: String
    ): Response<WorkoutResponse>
} 