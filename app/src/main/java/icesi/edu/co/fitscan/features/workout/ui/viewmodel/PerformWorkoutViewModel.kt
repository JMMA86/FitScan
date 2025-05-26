package icesi.edu.co.fitscan.features.workout.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import icesi.edu.co.fitscan.domain.usecases.IManageExercisesUseCase
import icesi.edu.co.fitscan.domain.usecases.IManageWorkoutExercisesUseCase
import icesi.edu.co.fitscan.domain.usecases.IManageWorkoutUseCase
import icesi.edu.co.fitscan.features.workout.ui.model.CurrentExercise
import icesi.edu.co.fitscan.features.workout.ui.model.NextExercise
import icesi.edu.co.fitscan.features.workout.ui.model.PerformWorkoutUiState
import icesi.edu.co.fitscan.features.workout.ui.model.RemainingExercise
import icesi.edu.co.fitscan.features.workout.ui.model.WorkoutUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class PerformWorkoutViewModel(
    private val performWorkoutUseCase: IManageWorkoutExercisesUseCase,
    private val exerciseUseCase: IManageExercisesUseCase,
    private val workoutUseCase: IManageWorkoutUseCase,
    private val workoutId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow<PerformWorkoutUiState>(PerformWorkoutUiState.Idle)
    val uiState: StateFlow<PerformWorkoutUiState> get() = _uiState

    private var _workoutState = WorkoutUiState()
    private var exercises: List<RemainingExercise> = emptyList()
    private var currentExerciseIndex: Int = 0
    private var isPaused: Boolean = false

    // Timer state
    private val _exerciseSeconds = MutableStateFlow(0)
    private var timerJob: Job? = null

    fun startWorkout() {
        _uiState.value = PerformWorkoutUiState.Loading
        viewModelScope.launch {
            exercises = loadExercises()
            val actualWorkout = loadWorkout()
            val formattedTime = getCurrentTimeFormatted()

            currentExerciseIndex = 0
            _exerciseSeconds.value = 0
            startTimer()

            val currentExercise = createCurrentExercise(
                exercises,
                currentExerciseIndex,
                formattedTime,
                _exerciseSeconds.value
            )
            val nextExercise = createNextExercise(exercises, currentExerciseIndex + 1)
            val remainingExercises = exercises.drop(1)

            _workoutState = _workoutState.copy(
                title = actualWorkout?.name.orEmpty(),
                subtitle = actualWorkout?.type.toString(),
                progress = "0/${exercises.size} ejercicios completados",
                currentExercise = currentExercise,
                nextExercise = nextExercise,
                remainingExercises = remainingExercises
            )

            _uiState.value = PerformWorkoutUiState.Success(_workoutState)
        }
    }

    fun endSet() {
        viewModelScope.launch {
            val updatedSeries = updateSeries(_workoutState.currentExercise.series)
            _workoutState = _workoutState.copy(
                currentExercise = _workoutState.currentExercise.copy(series = updatedSeries),
                progress = updateProgress()
            )
            _uiState.value = PerformWorkoutUiState.Success(_workoutState)
        }
    }

    fun goToNextExercise() {
        viewModelScope.launch {
            if (currentExerciseIndex < exercises.size - 1) {
                currentExerciseIndex++
                _exerciseSeconds.value = 0
                startTimer()
                updateCurrentAndNextExercises()
            }
        }
    }

    fun goToPreviousExercise() {
        viewModelScope.launch {
            if (currentExerciseIndex > 0) {
                currentExerciseIndex--
                _exerciseSeconds.value = 0
                startTimer()
                updateCurrentAndNextExercises()
            }
        }
    }

    fun pauseExercise() {
        isPaused = !isPaused
        if (isPaused) {
            timerJob?.cancel()
        } else {
            startTimer()
        }
    }

    fun skipToNextExercise() {
        goToNextExercise()
    }

    fun finishWorkout() {
        timerJob?.cancel()
        _uiState.value = PerformWorkoutUiState.Idle
    }

    private suspend fun loadExercises(): List<RemainingExercise> {
        val workoutUUID = UUID.fromString(workoutId)
        val exercisesResponse = performWorkoutUseCase.getWorkoutExercises(workoutUUID)
        return exercisesResponse.getOrNull().orEmpty().map { item ->
            val exercise = exerciseUseCase.getExerciseById(item.exerciseId)
            RemainingExercise(
                title = exercise.name.toString(),
                sets = item.sets.toString(),
                reps = item.reps.toString()
            )
        }
    }

    private suspend fun loadWorkout(): icesi.edu.co.fitscan.domain.model.Workout? {
        val firstWorkoutId = performWorkoutUseCase
            .getWorkoutExercises(UUID.fromString(workoutId))
            .getOrNull()
            ?.firstOrNull()
            ?.workoutId
        return firstWorkoutId?.let { workoutUseCase.getWorkoutById(it).getOrNull() }
    }

    private fun getCurrentTimeFormatted(): String {
        val currentTime = LocalTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return currentTime.format(formatter)
    }

    private fun createCurrentExercise(
        exercises: List<RemainingExercise>,
        index: Int,
        formattedTime: String,
        seconds: Int
    ): CurrentExercise {
        val elapsed = formatSeconds(seconds)
        return exercises.getOrNull(index)?.let {
            CurrentExercise(
                it.title,
                formattedTime,
                it.sets,
                "Tiempo transcurrido: $elapsed"
            )
        } ?: CurrentExercise()
    }

    private fun createNextExercise(
        exercises: List<RemainingExercise>,
        index: Int
    ): NextExercise {
        return exercises.getOrNull(index)?.let {
            NextExercise(it.title, it.sets.toInt(), it.reps.toInt())
        } ?: NextExercise()
    }

    private fun updateSeries(series: String): String {
        val parts = series.split(" ")
        return if (parts.size >= 3) {
            val completed = parts[0].toInt() + 1
            val total = parts[2]
            "$completed de $total"
        } else {
            val completed = series.toIntOrNull()?.plus(1) ?: 1
            "$completed"
        }
    }

    private fun updateProgress(): String {
        val progressParts = _workoutState.progress.split("/")
        val completed = progressParts[0].toInt() + 1
        val total = progressParts[1].split(" ")[0].toInt()
        return "$completed/$total ejercicios completados"
    }

    private fun updateCurrentAndNextExercises() {
        val formattedTime = getCurrentTimeFormatted()
        val current = createCurrentExercise(
            exercises,
            currentExerciseIndex,
            formattedTime,
            _exerciseSeconds.value
        )
        val next = createNextExercise(exercises, currentExerciseIndex + 1)
        val remaining = exercises.drop(currentExerciseIndex + 1)
        _workoutState = _workoutState.copy(
            currentExercise = current,
            nextExercise = next,
            remainingExercises = remaining,
            progress = "${currentExerciseIndex}/${exercises.size} ejercicios completados"
        )
        _uiState.value = PerformWorkoutUiState.Success(_workoutState)
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (!isPaused) {
                delay(1000)
                _exerciseSeconds.value += 1
                updateCurrentAndNextExercises()
            }
        }
    }

    private fun formatSeconds(seconds: Int): String {
        val min = seconds / 60
        val sec = seconds % 60
        return "%02d:%02d".format(min, sec)
    }
}