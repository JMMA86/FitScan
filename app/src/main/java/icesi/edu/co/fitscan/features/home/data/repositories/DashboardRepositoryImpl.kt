package icesi.edu.co.fitscan.features.home.data.repositories

import icesi.edu.co.fitscan.domain.model.DashboardResponse
import icesi.edu.co.fitscan.domain.repositories.IDashboardRepository
import icesi.edu.co.fitscan.features.home.data.dataSources.IDashboardDataSource

class DashboardRepositoryImpl(
    private val datasource: IDashboardDataSource
) : IDashboardRepository {

    override suspend fun getDashboardData(userId: String): Result<DashboardResponse> {
        return try {
            val response = datasource.getDashboardData(userId = userId)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 