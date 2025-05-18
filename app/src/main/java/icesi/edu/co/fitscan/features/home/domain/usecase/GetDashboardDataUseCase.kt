package icesi.edu.co.fitscan.features.home.domain.usecase

import icesi.edu.co.fitscan.features.home.domain.data.remote.response.DashboardResponse
import icesi.edu.co.fitscan.features.home.domain.service.DashboardService

class GetDashboardDataUseCase(private val dashboardService: DashboardService) {
    suspend operator fun invoke(userId: String): Result<DashboardResponse> {
        if (userId.isBlank()) {
            return Result.failure(IllegalArgumentException("User ID cannot be null or empty"))
        }
        return dashboardService.getDashboardData(userId)
    }
} 