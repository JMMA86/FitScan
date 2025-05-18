package icesi.edu.co.fitscan.features.home.ui.model

data class WeeklyMetrics(
    val trainingHours: String = "0h",
    val trainedDays: String = "0",
    val totalDistance: String = "0km"
)

data class RecentActivity(
    val title: String,
    val time: String,
    val level: String,
    val exercises: String
)

data class DashboardUiState(
    val weeklyMetrics: WeeklyMetrics = WeeklyMetrics(),
    val weeklyProgress: Float = 0f,
    val recentActivities: List<RecentActivity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) 