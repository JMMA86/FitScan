package icesi.edu.co.fitscan.features.home.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import icesi.edu.co.fitscan.features.common.data.remote.RetrofitInstance
import icesi.edu.co.fitscan.features.home.domain.repository.impl.DashboardRepositoryImpl
import icesi.edu.co.fitscan.features.home.domain.service.DashboardService
import icesi.edu.co.fitscan.features.home.domain.service.impl.DashboardServiceImpl
import icesi.edu.co.fitscan.features.home.ui.model.DashboardUiState
import icesi.edu.co.fitscan.features.home.ui.model.RecentActivity
import icesi.edu.co.fitscan.features.home.ui.model.WeeklyMetrics
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {
    private val dashboardService: DashboardService = DashboardServiceImpl(RetrofitInstance.dashboardRepository)
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    fun loadDashboardData(userId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            dashboardService.getDashboardData(userId).fold(
                onSuccess = { response ->
                    _uiState.value = DashboardUiState(
                        weeklyMetrics = WeeklyMetrics(
                            trainingHours = "${response.weeklyStats.trainingHours}h",
                            trainedDays = response.weeklyStats.trainedDays.toString(),
                            totalDistance = "${response.weeklyStats.totalDistance}km"
                        ),
                        weeklyProgress = response.weeklyProgress,
                        recentActivities = response.recentActivities.map { activity ->
                            RecentActivity(
                                title = activity.title,
                                time = activity.time,
                                level = activity.level,
                                exercises = activity.exercises
                            )
                        },
                        isLoading = false
                    )
                },
                onFailure = { error -> 
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Error al cargar los datos del dashboard"
                    )
                }
            )
        }
    }

    fun retryLoading(userId: String) {
        loadDashboardData(userId)
    }
} 