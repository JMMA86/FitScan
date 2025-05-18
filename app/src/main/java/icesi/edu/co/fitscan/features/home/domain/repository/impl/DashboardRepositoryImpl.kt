package icesi.edu.co.fitscan.features.home.domain.repository.impl

import icesi.edu.co.fitscan.features.home.domain.data.remote.response.DashboardResponse
import icesi.edu.co.fitscan.features.home.domain.repository.DashboardRepository
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface DashboardRepositoryImpl : DashboardRepository {
    @GET("items/workout")
    override suspend fun getDashboardData(
        @Header("Authorization") token: String,
        @Query("filter[customer_id][_eq]") userId: String
    ): Response<DashboardResponse>
}