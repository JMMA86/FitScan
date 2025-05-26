package icesi.edu.co.fitscan.features.statistics.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import icesi.edu.co.fitscan.domain.usecases.*
import icesi.edu.co.fitscan.features.common.ui.viewmodel.AppState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class StatisticsViewModel(
    private val fetchWeeklyStatsUseCase: IFetchWeeklyStatsUseCase,
    private val fetchWeightStatsUseCase: IFetchWeightStatsUseCase,
    private val fetchCaloriesStatsUseCase: IFetchCaloriesStatsUseCase,
) : ViewModel() {

    private val _caloriesAreaData = MutableStateFlow<List<Float>>(emptyList())
    val caloriesAreaData: StateFlow<List<Float>> = _caloriesAreaData.asStateFlow()

    private val _weightAreaData = MutableStateFlow<List<Float>>(emptyList())
    val weightAreaData: StateFlow<List<Float>> = _weightAreaData.asStateFlow()

    private val _labels = MutableStateFlow(listOf(""))
    val labels: StateFlow<List<String>> = _labels.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _currentWeek = MutableStateFlow(List(7) { 0f })
    val currentWeek: StateFlow<List<Float>> = _currentWeek.asStateFlow()

    private val _lastWeek = MutableStateFlow(List(7) { 0f })
    val lastWeek: StateFlow<List<Float>> = _lastWeek.asStateFlow()

    init {
        loadAllStatistics()
    }

    private fun loadAllStatistics() {
        val customerId = AppState.customerId ?: return
        viewModelScope.launch {

            try {
                fetchWeeklyStatsUseCase(customerId).onSuccess { stats ->
                    _currentWeek.value = stats.currentWeek
                    _lastWeek.value = stats.lastWeek
                }
            } catch (e: Exception) {
                _currentWeek.value = List(7) { 0f }
                _lastWeek.value = List(7) { 0f }
            }

            try {
                fetchCaloriesStatsUseCase(customerId).onSuccess { calories ->
                    _caloriesAreaData.value = calories
                }
            } catch (e: Exception) {
                _caloriesAreaData.value = List(6) { 0f }
            }

            try {
                fetchWeightStatsUseCase(customerId).onSuccess { weights ->
                    _weightAreaData.value = weights
                }
            } catch (e: Exception) {
                _weightAreaData.value = List(6) { 0f }
            }
        }
    }
}