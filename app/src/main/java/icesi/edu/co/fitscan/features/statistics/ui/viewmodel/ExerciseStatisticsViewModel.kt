package icesi.edu.co.fitscan.features.statistics.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import icesi.edu.co.fitscan.features.statistics.domain.service.ExerciseStatisticsService
import icesi.edu.co.fitscan.features.statistics.data.remote.ExerciseStatisticsRemoteDataSource
import icesi.edu.co.fitscan.features.auth.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import co.yml.charts.common.model.Point

class ExerciseStatisticsViewModel(
    private val service: ExerciseStatisticsService
) : ViewModel() {
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

    init {
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

    constructor() : this(
        ExerciseStatisticsService(
            RetrofitInstance.statisticsRepository
        )
    )
}
