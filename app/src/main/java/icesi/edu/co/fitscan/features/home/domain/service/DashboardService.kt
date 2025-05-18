package icesi.edu.co.fitscan.features.home.domain.service

import icesi.edu.co.fitscan.features.home.domain.data.remote.response.DashboardResponse

interface DashboardService {
    suspend fun getDashboardData(userId: String): Result<DashboardResponse>
} 