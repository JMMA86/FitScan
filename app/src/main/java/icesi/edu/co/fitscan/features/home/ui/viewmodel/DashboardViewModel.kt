package icesi.edu.co.fitscan.features.home.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import icesi.edu.co.fitscan.features.home.domain.usecase.GetDashboardDataUseCase
import icesi.edu.co.fitscan.features.home.ui.model.DashboardUiState
import icesi.edu.co.fitscan.features.home.ui.model.RecentActivity
import icesi.edu.co.fitscan.features.home.ui.model.WeeklyMetrics
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val getDashboardDataUseCase: GetDashboardDataUseCase? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        if (getDashboardDataUseCase == null) {
            // Preview mode - load mock data
            loadMockData()
        }
    }

    fun loadDashboardData(userId: String) {
        if (getDashboardDataUseCase == null) return
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            getDashboardDataUseCase(userId).fold(
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

    private fun loadMockData() {
        _uiState.value = DashboardUiState(
            weeklyMetrics = WeeklyMetrics(
                trainingHours = "12h",
                trainedDays = "4",
                totalDistance = "8.5km"
            ),
            weeklyProgress = 0.75f,
            recentActivities = listOf(
                RecentActivity(
                    title = "Sesión de cardio",
                    time = "45 min",
                    level = "Avanzado",
                    exercises = "8 Ejercicios"
                ),
                RecentActivity(
                    title = "Caminata",
                    time = "30 min",
                    level = "Intermedio",
                    exercises = "5 Ejercicios"
                )
            )
        )
    }
} 