package icesi.edu.co.fitscan.domain.model

import java.time.LocalDateTime
import java.time.ZoneId

data class DashboardResponse(
    val data: List<Workout>
) {
    val weeklyStats: WeeklyStats
        get() {
            val currentWeekWorkouts = data.filter { workout ->
                val workoutDate = workout.dateCreated?.toInstant()
                    ?.atZone(ZoneId.systemDefault())
                    ?.toLocalDateTime()
                val now = LocalDateTime.now()
                val weekStart = now.minusDays(now.dayOfWeek.value.toLong() - 1)
                workoutDate != null && workoutDate.isAfter(weekStart) && workoutDate.isBefore(now.plusDays(1))
            }

            return WeeklyStats(
                trainingHours = currentWeekWorkouts.sumOf { it.durationMinutes ?: 0 } / 60,
                trainedDays = currentWeekWorkouts.mapNotNull {
                    it.dateCreated?.toInstant()
                        ?.atZone(ZoneId.systemDefault())
                        ?.toLocalDate()
                        ?.toString()
                }.distinct().size,
                totalDistance = currentWeekWorkouts.sumOf { (it.durationMinutes ?: 0) * 0.1 }
            )
        }

    val weeklyProgress: Float
        get() {
            val currentWeekWorkouts = data.filter { workout ->
                val workoutDate = workout.dateCreated?.toInstant()
                    ?.atZone(ZoneId.systemDefault())
                    ?.toLocalDateTime()
                val now = LocalDateTime.now()
                val weekStart = now.minusDays(now.dayOfWeek.value.toLong() - 1)
                workoutDate != null && workoutDate.isAfter(weekStart) && workoutDate.isBefore(now.plusDays(1))
            }

            return (currentWeekWorkouts.mapNotNull {
                it.dateCreated?.toInstant()
                    ?.atZone(ZoneId.systemDefault())
                    ?.toLocalDate()
                    ?.toString()
            }.distinct().size / 5f).coerceAtMost(1f)
        }

    val recentActivities: List<Activity>
        get() = data.take(5).map { workout ->
            Activity(
                id = workout.id.toString(),
                title = workout.name,
                time = if (workout.durationMinutes != null && workout.durationMinutes > 0) 
                    "${workout.durationMinutes} min" 
                else 
                    "Sin duración",
                level = workout.difficulty ?: "Unknown",
                exercises = "Workout",
                type = workout.type.toString()
            )
        }
}