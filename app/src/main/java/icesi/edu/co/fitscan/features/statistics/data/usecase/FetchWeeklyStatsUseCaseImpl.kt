package icesi.edu.co.fitscan.features.statistics.data.usecase

import icesi.edu.co.fitscan.domain.model.StatisticsWeeklyStats
import icesi.edu.co.fitscan.domain.repositories.IStatisticsRepository
import icesi.edu.co.fitscan.domain.usecases.IFetchWeeklyStatsUseCase

class FetchWeeklyStatsUseCaseImpl(private val repository: IStatisticsRepository): IFetchWeeklyStatsUseCase {
    override suspend operator fun invoke(customerId: String): Result<StatisticsWeeklyStats> {
        return repository.getWorkedHoursPerDayForLast14Days()
    }
}
