package icesi.edu.co.fitscan.domain.usecases

import icesi.edu.co.fitscan.domain.model.StatisticsWeeklyStats

interface IFetchWeeklyStatsUseCase {
    suspend operator fun invoke(customerId: String): Result<StatisticsWeeklyStats>
}
