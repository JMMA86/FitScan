package icesi.edu.co.fitscan.features.home.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import icesi.edu.co.fitscan.domain.model.RecentActivity
import icesi.edu.co.fitscan.domain.model.WeeklyMetrics
import icesi.edu.co.fitscan.domain.usecases.IGetDashboardDataUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val getDashboardDataUseCase: IGetDashboardDataUseCase
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _weeklyMetrics = MutableStateFlow<WeeklyMetrics?>(null)
    val weeklyMetrics: StateFlow<WeeklyMetrics?> = _weeklyMetrics

    private val _weeklyProgress = MutableStateFlow(0f)
    val weeklyProgress: StateFlow<Float> = _weeklyProgress

    private val _recentActivities = MutableStateFlow<List<RecentActivity>>(emptyList())
    val recentActivities: StateFlow<List<RecentActivity>> = _recentActivities

    fun loadDashboardData(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            getDashboardDataUseCase(userId)
                .catch { e ->
                    _error.value = e.message ?: "Error desconocido al cargar el dashboard"
                    _isLoading.value = false
                }
                .collect { response ->
                    _weeklyMetrics.value = WeeklyMetrics(
                        trainingHours = "${response.weeklyStats.trainingHours}h",
                        trainedDays = response.weeklyStats.trainedDays.toString(),
                        totalDistance = "${response.weeklyStats.totalDistance}km"
                    )
                    _weeklyProgress.value = response.weeklyProgress
                    _recentActivities.value = response.recentActivities.map { activity ->
                        RecentActivity(
                            id = activity.id, // Propagar el id
                            title = activity.title,
                            time = activity.time,
                            level = activity.level,
                            exercises = activity.exercises
                        )
                    }
                    _isLoading.value = false
                }
        }
    }

    fun retryLoading(userId: String) {
        loadDashboardData(userId)
    }
}