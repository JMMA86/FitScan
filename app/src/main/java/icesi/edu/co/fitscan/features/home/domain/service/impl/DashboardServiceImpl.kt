package icesi.edu.co.fitscan.features.home.domain.service.impl

import icesi.edu.co.fitscan.features.common.ui.viewmodel.AppState
import icesi.edu.co.fitscan.features.home.domain.data.remote.response.DashboardResponse
import icesi.edu.co.fitscan.features.home.domain.repository.DashboardRepository
import icesi.edu.co.fitscan.features.home.domain.service.DashboardService

class DashboardServiceImpl(
    private val dashboardRepository: DashboardRepository
) : DashboardService {

    override suspend fun getDashboardData(userId: String): Result<DashboardResponse> {
        return try {
            val token = AppState.token ?: throw IllegalArgumentException("Token is null")
            val response = dashboardRepository.getDashboardData(token = "Bearer $token", userId = userId)
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