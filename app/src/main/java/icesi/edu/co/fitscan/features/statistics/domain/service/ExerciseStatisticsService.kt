package icesi.edu.co.fitscan.features.statistics.domain.service

import icesi.edu.co.fitscan.features.common.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import icesi.edu.co.fitscan.features.statistics.data.remote.ExerciseStatisticsRemoteDataSource
import icesi.edu.co.fitscan.features.statistics.data.remote.WorkoutSession
import icesi.edu.co.fitscan.features.statistics.data.remote.ProgressPhoto
import icesi.edu.co.fitscan.features.statistics.data.remote.dto.ExerciseItem
import icesi.edu.co.fitscan.features.statistics.data.remote.dto.ExerciseListResponse
import icesi.edu.co.fitscan.features.statistics.data.remote.dto.CompletedExerciseProgressDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.Duration

class ExerciseStatisticsService(
    private val remoteDataSource: ExerciseStatisticsRemoteDataSource = RetrofitInstance.statisticsRepository
) {
    var currentToken: String = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjAyMDRhMDVhLWRkN2MtNDFmYS05NWU1LTM4OGZiZmNiNmE2OCIsInJvbGUiOiJjOGI5MzgxNi1jOTZmLTRhNTEtYTZlNi0zYjgyZjZkODhmZGEiLCJhcHBfYWNjZXNzIjp0cnVlLCJhZG1pbl9hY2Nlc3MiOnRydWUsImlhdCI6MTc0NzYzMDE1MywiZXhwIjoxNzQ3NjMzNzUzLCJpc3MiOiJkaXJlY3R1cyJ9.ONp3vWWT2IVYjL0bFK4oFsdi61NQXieBdMNLv7uEijM"
    var currentCustomerId: String = "3ae128fe-5113-4195-b4eb-cdc5b0777298"

    private val _statisticsData = MutableStateFlow<List<Pair<String, Float>>>(emptyList())
    val statisticsData: StateFlow<List<Pair<String, Float>>> = _statisticsData

    fun updateStatistics(newData: List<Pair<String, Float>>) {
        _statisticsData.value = newData
    }

    suspend fun fetchProgressPhotos(): List<ProgressPhoto> = try {
        remoteDataSource.getProgressPhotos(currentToken, currentCustomerId).data
    } catch (e: Exception) { emptyList() }

    suspend fun fetchDistanceStats(): Float = try {
        val sessions = remoteDataSource.getDistanceStats(currentToken, currentCustomerId).data
        sessions.sumOf { it.distance_km?.toDouble() ?: 0.0 }.toFloat()
    } catch (e: Exception) { 0f }

    suspend fun fetchWeightStats(): Int = try {
        val customer = remoteDataSource.getCustomer(currentToken, currentCustomerId).data.firstOrNull()
        val bodyMeasureId = customer?.body_measure_id ?: ""
        val measures = remoteDataSource.getWeightStats(currentToken, bodyMeasureId).data
        measures.firstOrNull()?.weight_kg ?: 0
    } catch (e: Exception) { 0 }

    suspend fun fetchCaloriesStats(): Int = try {
        val sessions = remoteDataSource.getCaloriesStats(currentToken, currentCustomerId).data
        sessions.sumOf { it.calories_burned ?: 0 }
    } catch (e: Exception) { 0 }

    suspend fun fetchWeightMovedStats(): Int {
        return try {
            val sessions = remoteDataSource.getWorkoutSessions(currentToken, currentCustomerId).data
            val sessionIds = sessions.map { it.id }
            if (sessionIds.isEmpty()) return 0
            val completed = remoteDataSource.getWeightMovedStats(currentToken, sessionIds.joinToString(",")).data
            // Now use weight_kg for total weight moved
            completed.sumOf { ((it.sets ?: 0) * (it.reps ?: 0) * (it.weight_kg ?: 0)).toDouble() }.toFloat().toInt()
        } catch (e: Exception) {
            0
        }
    }

    suspend fun fetchWorkoutSessions(): List<WorkoutSession> = try {
        remoteDataSource.getWorkoutSessions(currentToken, currentCustomerId).data
    } catch (e: Exception) { emptyList() }

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

    // --- NEW: Aggregation for charts ---
    suspend fun getCaloriesPerDayForLast14Days(): Pair<List<Float>, List<Float>> {
        return try {
            val sessions = remoteDataSource.getCaloriesStats(currentToken, currentCustomerId).data
            val now = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            val caloriesByDay = (0..13).map { i ->
                val day = now.minusDays(i.toLong())
                val total = sessions.filter {
                    val start = LocalDateTime.parse(it.start_time, formatter)
                    start.toLocalDate() == day.toLocalDate()
                }.sumOf { (it.calories_burned ?: 0).toDouble() }.toFloat()
                total
            }.reversed()
            val currentWeek = caloriesByDay.takeLast(7)
            val lastWeek = caloriesByDay.take(7)
            Pair(currentWeek, lastWeek)
        } catch (e: Exception) {
            Pair(List(7) { 0f }, List(7) { 0f })
        }
    }

    suspend fun getWeightMovedPerDayForLast14Days(): Pair<List<Float>, List<Float>> {
        return try {
            val sessions = remoteDataSource.getWorkoutSessions(currentToken, currentCustomerId).data
            val sessionIds = sessions.map { it.id }
            if (sessionIds.isEmpty()) return Pair(List(7) { 0f }, List(7) { 0f })
            val completed = remoteDataSource.getWeightMovedStats(currentToken, sessionIds.joinToString(",")).data
            val now = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            val weightByDay = (0..13).map { i ->
                val day = now.minusDays(i.toLong())
                val sessionIdsForDay = sessions.filter {
                    val start = LocalDateTime.parse(it.start_time, formatter)
                    start.toLocalDate() == day.toLocalDate()
                }.map { it.id }
                val total = completed.filter { it.workout_session_id in sessionIdsForDay }
                    .sumOf { ((it.sets ?: 0) * (it.reps ?: 0) * (it.weight_kg ?: 0)).toDouble() }.toFloat()
                total
            }.reversed()
            val currentWeek = weightByDay.takeLast(7)
            val lastWeek = weightByDay.take(7)
            Pair(currentWeek, lastWeek)
        } catch (e: Exception) {
            Pair(List(7) { 0f }, List(7) { 0f })
        }
    }

    suspend fun getCaloriesAreaData(): List<Float> {
        return try {
            val sessions = remoteDataSource.getCaloriesStats(currentToken, currentCustomerId).data
            val now = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            (0..5).map { i ->
                val start = now.minusDays((5 - i) * 7L)
                val end = start.plusDays(6)
                val total = sessions.filter {
                    val sessionTime = LocalDateTime.parse(it.start_time, formatter)
                    !sessionTime.isBefore(start) && !sessionTime.isAfter(end)
                }.sumOf { (it.calories_burned ?: 0).toDouble() }.toFloat()
                total
            }
        } catch (e: Exception) {
            List(6) { 0f }
        }
    }

    suspend fun getWeightAreaData(): List<Float> {
        return try {
            val sessions = remoteDataSource.getWorkoutSessions(currentToken, currentCustomerId).data
            val sessionIds = sessions.map { it.id }
            if (sessionIds.isEmpty()) return List(6) { 0f }
            val completed = remoteDataSource.getWeightMovedStats(currentToken, sessionIds.joinToString(",")).data
            val now = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            (0..5).map { i ->
                val start = now.minusDays((5 - i) * 7L)
                val end = start.plusDays(6)
                val sessionIdsForPeriod = sessions.filter {
                    val sessionTime = LocalDateTime.parse(it.start_time, formatter)
                    !sessionTime.isBefore(start) && !sessionTime.isAfter(end)
                }.map { it.id }
                val total = completed.filter { it.workout_session_id in sessionIdsForPeriod }
                    .sumOf { ((it.sets ?: 0) * (it.reps ?: 0) * (it.weight_kg ?: 0)).toDouble() }.toFloat()
                total
            }
        } catch (e: Exception) {
            List(6) { 0f }
        }
    }

    suspend fun getWorkedHoursPerDayForLast14Days(): Pair<List<Float>, List<Float>> {
        return try {
            val sessions = remoteDataSource.getWorkoutSessions(currentToken, currentCustomerId).data
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
            Pair(currentWeek, lastWeek)
        } catch (e: Exception) {
            Pair(List(7) { 0f }, List(7) { 0f })
        }
    }

    suspend fun fetchExerciseProgress(
        exerciseId: String,
        fromDate: String?,
        toDate: String?
    ): List<Pair<String, Float>> {
        return try {
            val result = remoteDataSource.getCompletedExercisesForProgress(
                currentToken,
                currentCustomerId,
                exerciseId,
                fromDate,
                toDate
            ).data
            // If no data, return empty
            if (result.isEmpty()) return emptyList()

            // Parse all dates and find min/max
            val formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            val allDates = result.mapNotNull { it.workout_session_id?.start_time }
                .mapNotNull {
                    try { java.time.LocalDateTime.parse(it, formatter) } catch (e: Exception) { null }
                }
            if (allDates.isEmpty()) return emptyList()
            val minDate = fromDate?.let {
                try { java.time.LocalDateTime.parse(it + "T00:00:00", formatter) } catch (e: Exception) { allDates.minOrNull() }
            } ?: allDates.minOrNull()
            val maxDate = toDate?.let {
                try { java.time.LocalDateTime.parse(it + "T23:59:59", formatter) } catch (e: Exception) { allDates.maxOrNull() }
            } ?: allDates.maxOrNull()
            if (minDate == null || maxDate == null) return emptyList()
            val totalDuration = java.time.Duration.between(minDate, maxDate)
            val intervalDuration = if (totalDuration.toMillis() > 0) totalDuration.dividedBy(7) else java.time.Duration.ofDays(1)

            // Group by interval (7 parts)
            val intervals = (0 until 7).map { i ->
                val start = minDate.plus(intervalDuration.multipliedBy(i.toLong()))
                val end = if (i == 6) maxDate else start.plus(intervalDuration)
                start to end
            }
            val grouped = intervals.map { (start, end) ->
                val entries = result.filter {
                    val dateStr = it.workout_session_id?.start_time
                    val date = try { java.time.LocalDateTime.parse(dateStr, formatter) } catch (e: Exception) { null }
                    date != null && (date.isEqual(start) || (date.isAfter(start) && date.isBefore(end)))
                }
                val maxWeight = entries.maxOfOrNull { it.weight_kg ?: 0 } ?: 0
                // Label: e.g., may-16, abr-05
                val label = "${end.month.name.lowercase().take(3)}-${end.dayOfMonth.toString().padStart(2, '0')}"
                label to maxWeight.toFloat()
            }
            grouped
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun fetchAllExercises(): List<Pair<String, String>> {
        return try {
            remoteDataSource.getAllExercises(currentToken).data.map { it.name to it.id }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
