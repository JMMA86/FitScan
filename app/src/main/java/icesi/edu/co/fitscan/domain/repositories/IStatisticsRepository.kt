package icesi.edu.co.fitscan.domain.repositories

import icesi.edu.co.fitscan.domain.model.*

interface IStatisticsRepository {
    suspend fun fetchWeightStats(): Result<List<Float>>
    suspend fun fetchCaloriesStats(): Result<List<Float>>
    suspend fun getWorkedHoursPerDayForLast14Days(): Result<StatisticsWeeklyStats>
}
