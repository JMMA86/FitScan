package icesi.edu.co.fitscan.features.home.domain.data.remote.response

data class Activity(
    val id: String,
    val title: String,
    val time: String,
    val level: String,
    val exercises: String,
    val type: String
)

data class WeeklyStats(
    val trainingHours: Int,
    val trainedDays: Int,
    val totalDistance: Double
)

data class Workout(
    val id: String,
    val customer_id: String,
    val name: String,
    val type: String,
    val duration_minutes: Int,
    val difficulty: String,
    val date_created: String
)

data class DashboardResponse(
    val data: List<Workout>
) {
    val weeklyStats: WeeklyStats
        get() {
            val currentWeekWorkouts = data.filter { workout ->
                // Filtrar workouts de la semana actual
                val workoutDate = java.time.LocalDateTime.parse(workout.date_created)
                val now = java.time.LocalDateTime.now()
                val weekStart = now.minusDays(now.dayOfWeek.value.toLong() - 1)
                workoutDate.isAfter(weekStart) && workoutDate.isBefore(now.plusDays(1))
            }

            return WeeklyStats(
                trainingHours = currentWeekWorkouts.sumOf { it.duration_minutes } / 60,
                trainedDays = currentWeekWorkouts.map { it.date_created.substring(0, 10) }.distinct().size,
                totalDistance = currentWeekWorkouts.sumOf { it.duration_minutes } * 0.1 // Estimación aproximada
            )
        }

    val weeklyProgress: Float
        get() {
            val currentWeekWorkouts = data.filter { workout ->
                val workoutDate = java.time.LocalDateTime.parse(workout.date_created)
                val now = java.time.LocalDateTime.now()
                val weekStart = now.minusDays(now.dayOfWeek.value.toLong() - 1)
                workoutDate.isAfter(weekStart) && workoutDate.isBefore(now.plusDays(1))
            }

            return (currentWeekWorkouts.map { it.date_created.substring(0, 10) }.distinct().size / 5f).coerceAtMost(1f)
        }

    val recentActivities: List<Activity>
        get() = data.take(5).map { workout ->
            Activity(
                id = workout.id,
                title = workout.name,
                time = "${workout.duration_minutes} min",
                level = workout.difficulty,
                exercises = "Workout",
                type = workout.type
            )
        }
} 