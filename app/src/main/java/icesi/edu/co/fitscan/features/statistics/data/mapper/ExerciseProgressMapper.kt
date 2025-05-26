package icesi.edu.co.fitscan.features.statistics.data.mapper

import icesi.edu.co.fitscan.domain.model.ExerciseProgressPoint
import icesi.edu.co.fitscan.features.statistics.data.dto.CompletedExerciseProgressDto

object ExerciseProgressMapper {
    fun toDomainList(
        dtoList: List<CompletedExerciseProgressDto>,
        fromDate: String?,
        toDate: String?
    ): List<ExerciseProgressPoint> {
        if (dtoList.isEmpty()) return emptyList()

        // Parse all dates and find min/max
        val formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val allDates = dtoList.mapNotNull { it.workout_session_id?.start_time }
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
            val entries = dtoList.filter {
                val dateStr = it.workout_session_id?.start_time
                val date = try { java.time.LocalDateTime.parse(dateStr, formatter) } catch (e: Exception) { null }
                date != null && (date.isEqual(start) || (date.isAfter(start) && date.isBefore(end)))
            }
            val maxWeight = entries.maxOfOrNull { it.weight_kg ?: 0 } ?: 0
            // Label: e.g., may-16, abr-05
            val label = "${end.month.name.lowercase().take(3)}-${end.dayOfMonth.toString().padStart(2, '0')}"
            ExerciseProgressPoint(label, maxWeight.toFloat())
        }
        return grouped
    }
}
