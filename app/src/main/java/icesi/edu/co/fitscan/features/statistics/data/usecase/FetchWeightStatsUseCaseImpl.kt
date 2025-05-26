package icesi.edu.co.fitscan.features.statistics.data.usecase

import icesi.edu.co.fitscan.domain.repositories.IStatisticsRepository
import icesi.edu.co.fitscan.domain.usecases.IFetchWeightStatsUseCase

class FetchWeightStatsUseCaseImpl(private val repository: IStatisticsRepository): IFetchWeightStatsUseCase {
    override suspend operator fun invoke(customerId: String) = repository.fetchWeightStats()
}
