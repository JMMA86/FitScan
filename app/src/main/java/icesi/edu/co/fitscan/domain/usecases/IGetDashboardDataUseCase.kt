package icesi.edu.co.fitscan.domain.usecases

import icesi.edu.co.fitscan.domain.model.DashboardResponse
import kotlinx.coroutines.flow.Flow

interface IGetDashboardDataUseCase {
    suspend operator fun invoke(userId: String): Flow<DashboardResponse>
}