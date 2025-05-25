package icesi.edu.co.fitscan.domain.repositories

import icesi.edu.co.fitscan.domain.model.DashboardResponse

interface IDashboardRepository {
    suspend fun getDashboardData(userId: String): Result<DashboardResponse>
} 