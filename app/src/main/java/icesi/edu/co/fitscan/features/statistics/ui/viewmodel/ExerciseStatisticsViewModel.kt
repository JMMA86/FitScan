package icesi.edu.co.fitscan.features.statistics.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import icesi.edu.co.fitscan.features.statistics.domain.service.ExerciseStatisticsService
import icesi.edu.co.fitscan.features.statistics.data.remote.ProgressPhoto
import icesi.edu.co.fitscan.features.common.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import co.yml.charts.common.model.Point

class ExerciseStatisticsViewModel(
    private val service: ExerciseStatisticsService = ExerciseStatisticsService()
) : ViewModel() {
    // Token and customerId for testing
    var currentToken: String
        get() = service.currentToken
        set(value) { service.currentToken = value }
    var currentCustomerId: String
        get() = service.currentCustomerId
        set(value) { service.currentCustomerId = value }

    private val _progressPhotos = MutableStateFlow<List<ProgressPhoto>>(emptyList())
    val progressPhotos: StateFlow<List<ProgressPhoto>> = _progressPhotos

    private val _distanceKm = MutableStateFlow(0f)
    val distanceKm: StateFlow<Float> = _distanceKm

    private val _weightLost = MutableStateFlow(0f)
    val weightLost: StateFlow<Float> = _weightLost

    private val _weightLostProgress = MutableStateFlow(0f)
    val weightLostProgress: StateFlow<Float> = _weightLostProgress

    private val _caloriesAreaData = MutableStateFlow<List<Float>>(emptyList())
    val caloriesAreaData: StateFlow<List<Float>> = _caloriesAreaData

    private val _weightAreaData = MutableStateFlow<List<Float>>(emptyList())
    val weightAreaData: StateFlow<List<Float>> = _weightAreaData

    private val _statisticsData = MutableStateFlow(listOf("Point" to 0.0))
    val statisticsData: StateFlow<List<Pair<String, Double>>> = _statisticsData

    private val _pointsData = MutableStateFlow<List<Point>>(listOf(Point(0.0f, 0.0f)))
    val pointsData: StateFlow<List<Point>> = _pointsData

    private val _labels = MutableStateFlow<List<String>>(listOf(""))
    val labels: StateFlow<List<String>> = _labels

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _isDropdownExpanded = MutableStateFlow(false)
    val isDropdownExpanded: StateFlow<Boolean> = _isDropdownExpanded.asStateFlow()

    private val _selectedRange = MutableStateFlow("12 Months")
    val selectedRange: StateFlow<String> = _selectedRange.asStateFlow()

    private var lastFetchedMonths: Int? = null // Track the last fetched range to prevent duplicate requests

    private val _currentWeek = MutableStateFlow<List<Float>>(List(7) { 0f })
    val currentWeek: StateFlow<List<Float>> = _currentWeek

    private val _lastWeek = MutableStateFlow<List<Float>>(List(7) { 0f })
    val lastWeek: StateFlow<List<Float>> = _lastWeek

    init {
        loadAllStatistics()
        fetchStatisticsForRange(12) // Default to 12 months
        Log.e(">>>", "again")
    }

    fun setDropdownExpanded(expanded: Boolean) {
        _isDropdownExpanded.value = expanded
    }

    fun fetchStatisticsForRange(months: Int) {
        if (_isLoading.value || lastFetchedMonths == months) return // Prevent multiple fetches for the same range
        lastFetchedMonths = months
        _selectedRange.value = when (months) {
            1 -> "1 mes"
            3 -> "3 meses"
            6 -> "6 meses"
            12 -> "12 meses"
            else -> "12 meses"
        }
        val rangeEnd = LocalDateTime.now()
        val rangeStart = rangeEnd.minusMonths(months.toLong())
        fetchStatistics(rangeStart, rangeEnd)
    }

    private fun fetchStatistics(rangeStart: LocalDateTime, rangeEnd: LocalDateTime) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val sessions = service.fetchWorkoutSessions()
                val processedData = service.fetchAndProcessStatistics(sessions, rangeStart, rangeEnd)
                _statisticsData.value = processedData
                _pointsData.value = processedData.mapIndexed { index, data ->
                    Point(index.toFloat(), data.second.toFloat())
                }
                _labels.value = processedData.map { it.first }
            } catch (e: Exception) {
                // Handle exception if needed
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadAllStatistics() {
        viewModelScope.launch {
            try {
                _progressPhotos.value = service.fetchProgressPhotos()
            } catch (e: Exception) { _progressPhotos.value = emptyList() }
            try {
                _distanceKm.value = service.fetchDistanceStats()
            } catch (e: Exception) { _distanceKm.value = 0f }
            try {
                _weightLost.value = service.fetchWeightStats().toFloat()
                _weightLostProgress.value = 0.8f // TODO: Calculate real progress
            } catch (e: Exception) { _weightLost.value = 0f; _weightLostProgress.value = 0f }
            try {
                val (caloriesCurrent, caloriesLast) = service.getCaloriesPerDayForLast14Days()
                _currentWeek.value = caloriesCurrent
                _lastWeek.value = caloriesLast
            } catch (e: Exception) {
                _currentWeek.value = List(7) { 0f }
                _lastWeek.value = List(7) { 0f }
            }
            try {
                _caloriesAreaData.value = service.getCaloriesAreaData()
            } catch (e: Exception) { _caloriesAreaData.value = List(6) { 0f } }
            try {
                _weightAreaData.value = service.getWeightAreaData()
            } catch (e: Exception) { _weightAreaData.value = List(6) { 0f } }
        }
    }
}
