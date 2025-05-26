package icesi.edu.co.fitscan.features.statistics.data.repositories

import icesi.edu.co.fitscan.domain.model.StatisticsWeeklyStats
import icesi.edu.co.fitscan.domain.repositories.IStatisticsRepository
import icesi.edu.co.fitscan.features.statistics.data.remote.StatisticsRemoteDataSource
import icesi.edu.co.fitscan.features.common.ui.viewmodel.AppState
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class StatisticsRepositoryImpl(
    private val remoteDataSource: StatisticsRemoteDataSource
) : IStatisticsRepository {
    private val customerId = AppState.customerId ?: ""

    override suspend fun fetchWeightStats(): Result<List<Float>> {
        return try {
            val sessions = remoteDataSource.getWorkoutSessions(customerId).data
            val sessionIds = sessions.map { it.id }
            if (sessionIds.isEmpty()) return Result.success(List(6) { 0f })
            val completed = remoteDataSource.getWeightMovedStats(sessionIds.joinToString(",")).data
            val now = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            val result = (0..5).map { i ->
                val start = now.minusDays((5 - i) * 7L)
                val end = start.plusDays(6)
                val sessionIdsForPeriod = sessions.filter {
                    val sessionTime = LocalDateTime.parse(it.start_time, formatter)
                    !sessionTime.isBefore(start) && !sessionTime.isAfter(end)
                }.map { it.id }

                completed.filter { sessionIdsForPeriod.contains(it.workout_session_id?.id) }
                    .sumOf { ((it.sets ?: 0) * (it.reps ?: 0) * (it.weight_kg ?: 0)).toDouble() }
                    .toFloat()
            }
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun fetchCaloriesStats(): Result<List<Float>> {
        return try {
            val sessions = remoteDataSource.getCaloriesStats(customerId).data
            val now = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            val result = (0..5).map { i ->
                val start = now.minusDays((5 - i) * 7L)
                val end = start.plusDays(6)
                val total = sessions.filter {
                    val sessionTime = LocalDateTime.parse(it.start_time, formatter)
                    !sessionTime.isBefore(start) && !sessionTime.isAfter(end)
                }.sumOf { (it.calories_burned).toDouble() }.toFloat()
                total
            }
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getWorkedHoursPerDayForLast14Days(): Result<StatisticsWeeklyStats> {
        return try {
            val actualCustomerId = AppState.customerId ?: ""
            val sessions = remoteDataSource.getWorkoutSessions(actualCustomerId).data
            val now = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            val hoursByDay = (0..13).map { i ->
                val day = now.minusDays(i.toLong())
                val total = sessions.filter {
                    val start = LocalDateTime.parse(it.start_time, formatter)
                    start.toLocalDate() == day.toLocalDate()
                }.sumOf {
                    val start = LocalDateTime.parse(it.start_time, formatter)
                    val end = LocalDateTime.parse(it.end_time, formatter)
                    java.time.Duration.between(start, end).toMinutes().toDouble() / 60.0
                }.toFloat()
                total
            }.reversed()
            val currentWeek = hoursByDay.takeLast(7)
            val lastWeek = hoursByDay.take(7)
            Result.success(StatisticsWeeklyStats(currentWeek, lastWeek))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
