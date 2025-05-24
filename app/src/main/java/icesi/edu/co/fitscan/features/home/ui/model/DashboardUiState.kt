package icesi.edu.co.fitscan.features.home.ui.model

import icesi.edu.co.fitscan.domain.model.RecentActivity
import icesi.edu.co.fitscan.domain.model.WeeklyMetrics

data class DashboardUiState(
    val weeklyMetrics: WeeklyMetrics = WeeklyMetrics(),
    val weeklyProgress: Float = 0f,
    val recentActivities: List<RecentActivity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) 