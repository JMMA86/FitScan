package icesi.edu.co.fitscan.features.statistics.domain.usecase

import icesi.edu.co.fitscan.features.statistics.domain.service.ExerciseStatisticsService
import kotlinx.coroutines.flow.StateFlow

class ExerciseStatisticsUseCase(private val service: ExerciseStatisticsService) {
    val statisticsData: StateFlow<List<Pair<String, Float>>> = service.statisticsData

    fun updateStatistics(newData: List<Pair<String, Float>>) {
        service.updateStatistics(newData)
    }
}
