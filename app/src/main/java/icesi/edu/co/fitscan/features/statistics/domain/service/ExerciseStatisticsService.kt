package icesi.edu.co.fitscan.features.statistics.domain.service

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import icesi.edu.co.fitscan.features.statistics.data.remote.ExerciseStatisticsRemoteDataSource
import icesi.edu.co.fitscan.features.statistics.data.remote.WorkoutSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.Duration

class ExerciseStatisticsService(
    private val remoteDataSource: ExerciseStatisticsRemoteDataSource
) {
    private val _statisticsData = MutableStateFlow<List<Pair<String, Float>>>(emptyList())
    val statisticsData: StateFlow<List<Pair<String, Float>>> = _statisticsData

    fun updateStatistics(newData: List<Pair<String, Float>>) {
        _statisticsData.value = newData
    }

    suspend fun fetchWorkoutSessions() = withContext(Dispatchers.IO) {
        remoteDataSource.getWorkoutSessions().data
    }

    suspend fun fetchAndProcessStatistics(
        sessions: List<WorkoutSession>,
        rangeStart: LocalDateTime,
        rangeEnd: LocalDateTime
    ): List<Pair<String, Double>> {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val filteredSessions = sessions.filter {
            val startTime = LocalDateTime.parse(it.start_time, formatter)
            startTime.isAfter(rangeStart) && startTime.isBefore(rangeEnd)
        }

        val intervalDuration = Duration.between(rangeStart, rangeEnd).dividedBy(6)
        val cumulativeDurations = mutableListOf<Pair<String, Double>>()
        var currentIntervalStart = rangeStart
        var currentIntervalEnd = rangeStart.plus(intervalDuration)
        var currentIntervalSum = 0.0

        filteredSessions.sortedBy {
            LocalDateTime.parse(it.start_time, formatter)
        }.forEach { session ->
            val start = LocalDateTime.parse(session.start_time, formatter)
            val end = LocalDateTime.parse(session.end_time, formatter)
            val sessionDuration = Duration.between(start, end).toMinutes().toDouble() / 60

            while (start.isAfter(currentIntervalEnd)) {
            val formattedDate = "${currentIntervalStart.month.name.lowercase().take(3)}-${currentIntervalStart.dayOfMonth}"
            cumulativeDurations.add(formattedDate to currentIntervalSum)
            currentIntervalStart = currentIntervalEnd
            currentIntervalEnd = currentIntervalEnd.plus(intervalDuration)
            currentIntervalSum = 0.0
            }

            currentIntervalSum += sessionDuration
        }

        // Add the last interval if not already added
        if (cumulativeDurations.size < 6) {
            val formattedDate = "${currentIntervalStart.month.name.lowercase().take(3)}-${currentIntervalStart.dayOfMonth}"
            cumulativeDurations.add(formattedDate to currentIntervalSum)
        }

        // Ensure exactly 6 points by filling empty intervals if necessary
        while (cumulativeDurations.size < 6) {
            currentIntervalStart = currentIntervalEnd
            currentIntervalEnd = currentIntervalEnd.plus(intervalDuration)
            val formattedDate = "${currentIntervalStart.month.name.lowercase().take(3)}-${currentIntervalStart.dayOfMonth}"
            cumulativeDurations.add(formattedDate to 0.0)
        }

        return cumulativeDurations
    }
}
