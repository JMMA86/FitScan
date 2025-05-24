package icesi.edu.co.fitscan.features.home.data.usecases

import icesi.edu.co.fitscan.domain.model.DashboardResponse
import icesi.edu.co.fitscan.domain.repositories.IDashboardRepository
import icesi.edu.co.fitscan.domain.usecases.IGetDashboardDataUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetDashboardDataUseCaseImpl(
    private val dashboardRepository: IDashboardRepository
) : IGetDashboardDataUseCase {
    override suspend operator fun invoke(userId: String): Flow<DashboardResponse> = flow {
        if (userId.isBlank()) {
            throw IllegalArgumentException("User ID cannot be null or empty")
        }

        val response = dashboardRepository.getDashboardData(userId).getOrThrow()
        emit(response)
    }
}