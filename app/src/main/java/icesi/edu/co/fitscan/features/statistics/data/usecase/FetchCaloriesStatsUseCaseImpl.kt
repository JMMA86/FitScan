package icesi.edu.co.fitscan.features.statistics.data.usecase

import icesi.edu.co.fitscan.domain.repositories.IStatisticsRepository
import icesi.edu.co.fitscan.domain.usecases.IFetchCaloriesStatsUseCase

class FetchCaloriesStatsUseCaseImpl(private val repository: IStatisticsRepository): IFetchCaloriesStatsUseCase {
    override suspend operator fun invoke(customerId: String) = repository.fetchCaloriesStats()
}
