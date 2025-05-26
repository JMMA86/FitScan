package icesi.edu.co.fitscan.features.statistics.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import icesi.edu.co.fitscan.domain.usecases.IFetchExerciseProgressUseCase
import icesi.edu.co.fitscan.domain.usecases.IFetchAllExercisesUseCase
import icesi.edu.co.fitscan.domain.model.ExerciseItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ExerciseProgressViewModel(
    private val fetchExerciseProgressUseCase: IFetchExerciseProgressUseCase,
    private val fetchAllExercisesUseCase: IFetchAllExercisesUseCase
) : ViewModel() {

    private val _selectedExercise = MutableStateFlow<String?>(null)
    val selectedExercise: StateFlow<String?> = _selectedExercise

    private val _exerciseId = MutableStateFlow<String?>(null)
    val exerciseId: StateFlow<String?> = _exerciseId

    private val _availableExercises = MutableStateFlow<List<ExerciseItem>>(emptyList())
    val availableExercises: StateFlow<List<ExerciseItem>> = _availableExercises

    private val _timeRange = MutableStateFlow(TimeRange.LAST_WEEK)
    val timeRange: StateFlow<TimeRange> = _timeRange

    private val _chartData = MutableStateFlow<List<Float>>(emptyList())
    val chartData: StateFlow<List<Float>> = _chartData

    private val _dateLabels = MutableStateFlow<List<String>>(emptyList())
    val dateLabels: StateFlow<List<String>> = _dateLabels

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        fetchAvailableExercises()
    }

    private fun fetchAvailableExercises() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = fetchAllExercisesUseCase()
                result.onSuccess { exercises ->
                    _availableExercises.value = exercises
                }.onFailure {
                    _availableExercises.value = emptyList()
                    _error.value = it.message
                }
            } catch (e: Exception) {
                _availableExercises.value = emptyList()
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setExercise(exercise: String) {
        _selectedExercise.value = exercise
        val id = _availableExercises.value.find { it.name == exercise }?.id
        _exerciseId.value = id
        fetchProgress()
    }

    fun setTimeRange(range: TimeRange) {
        _timeRange.value = range
        fetchProgress()
    }

    private fun fetchProgress() {
        val exerciseId = _exerciseId.value ?: return
        val (fromDate, toDate) = getDateRange(_timeRange.value)

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = fetchExerciseProgressUseCase(exerciseId, fromDate?:"", toDate?:"")
                result.onSuccess { progressPoints ->
                    _chartData.value = progressPoints.map { it.maxWeight }
                    _dateLabels.value = progressPoints.map { it.dateLabel }
                }.onFailure {
                    _chartData.value = emptyList()
                    _dateLabels.value = emptyList()
                    _error.value = it.message
                }
            } catch (e: Exception) {
                _chartData.value = emptyList()
                _dateLabels.value = emptyList()
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun getDateRange(range: TimeRange): Pair<String?, String?> {
        val now = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return when (range) {
            TimeRange.LAST_WEEK -> now.minusDays(6).format(formatter) to now.format(formatter)
            TimeRange.LAST_MONTH -> now.minusMonths(1).format(formatter) to now.format(formatter)
            TimeRange.LAST_3_MONTHS -> now.minusMonths(3).format(formatter) to now.format(formatter)
            TimeRange.LAST_6_MONTHS -> now.minusMonths(6).format(formatter) to now.format(formatter)
            TimeRange.LAST_12_MONTHS -> now.minusMonths(12).format(formatter) to now.format(formatter)
        }
    }
}

enum class TimeRange {
    LAST_WEEK,
    LAST_MONTH,
    LAST_3_MONTHS,
    LAST_6_MONTHS,
    LAST_12_MONTHS
}