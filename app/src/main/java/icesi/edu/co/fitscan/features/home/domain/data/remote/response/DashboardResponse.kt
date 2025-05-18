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

data class DashboardResponse(
    val weeklyStats: WeeklyStats,
    val weeklyProgress: Float,
    val recentActivities: List<Activity>
) 