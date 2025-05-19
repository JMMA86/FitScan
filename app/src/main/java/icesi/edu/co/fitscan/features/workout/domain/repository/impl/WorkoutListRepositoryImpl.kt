package icesi.edu.co.fitscan.features.workout.domain.repository.impl

import icesi.edu.co.fitscan.features.workout.domain.data.remote.response.WorkoutResponse
import icesi.edu.co.fitscan.features.workout.domain.repository.WorkoutListRepository
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

class WorkoutListRepositoryImpl(
    private val api: WorkoutListApi
) : WorkoutListRepository {

    override suspend fun getWorkouts(token: String, customerId: String): Response<WorkoutResponse> {
        return api.getWorkouts("Bearer $token", customerId)
    }
}

interface WorkoutListApi {
    @GET("items/workout")
    suspend fun getWorkouts(
        @Header("Authorization") token: String,
        @Query("filter[customer_id][_eq]") customerId: String
    ): Response<WorkoutResponse>
} 