package icesi.edu.co.fitscan.features.home.data.dataSources

import icesi.edu.co.fitscan.domain.model.DashboardResponse
import retrofit2.Response
import retrofit2.http.*

@JvmSuppressWildcards
interface IDashboardDataSource {

    companion object {
        const val BASE_PATH = "items"
    }

    @GET("$BASE_PATH/workout")
    suspend fun getDashboardData(
        @Query("filter[customer_id][_eq]") userId: String
    ): Response<DashboardResponse>
}