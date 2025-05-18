package icesi.edu.co.fitscan.features.home.domain.repository

import icesi.edu.co.fitscan.features.home.domain.data.remote.response.DashboardResponse
import retrofit2.Response

interface DashboardRepository {
    suspend fun getDashboardData(token: String, userId: String): Response<DashboardResponse>
} 