package icesi.edu.co.fitscan.features.workout.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import icesi.edu.co.fitscan.domain.model.CompletedExercise
import icesi.edu.co.fitscan.domain.model.WorkoutSession
import icesi.edu.co.fitscan.domain.usecases.IManageCompleteExerciseUseCase
import icesi.edu.co.fitscan.domain.usecases.IManageExercisesUseCase
import icesi.edu.co.fitscan.domain.usecases.IManageWorkoutExercisesUseCase
import icesi.edu.co.fitscan.domain.usecases.IManageWorkoutSessionUseCase
import icesi.edu.co.fitscan.domain.usecases.IManageWorkoutUseCase
import icesi.edu.co.fitscan.features.common.ui.viewmodel.AppState
import icesi.edu.co.fitscan.features.workout.ui.model.CurrentExercise
import icesi.edu.co.fitscan.features.workout.ui.model.NextExercise
import icesi.edu.co.fitscan.features.workout.ui.model.PerformWorkoutUiState
import icesi.edu.co.fitscan.features.workout.ui.model.RemainingExercise
import icesi.edu.co.fitscan.features.workout.ui.model.WorkoutUiState
import icesi.edu.co.fitscan.features.workout.ui.util.TimerManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.util.UUID

//TODO: Add error handling and loading states for better user experience.
//TODO: Implement a more robust timer mechanism that can handle edge cases like app backgrounding or interruptions.
class PerformWorkoutViewModel(
    private val performWorkoutUseCase: IManageWorkoutExercisesUseCase,
    private val exerciseUseCase: IManageExercisesUseCase,
    private val workoutUseCase: IManageWorkoutUseCase,
    private val workoutSessionUseCase: IManageWorkoutSessionUseCase,
    private val completedExerciseUseCase: IManageCompleteExerciseUseCase,
    private val workoutId: String,
) : ViewModel() {
    // StateFlow to manage the UI state of the workout session
    private val _uiState = MutableStateFlow<PerformWorkoutUiState>(PerformWorkoutUiState.Idle)
    val uiState: StateFlow<PerformWorkoutUiState> get() = _uiState

    // StateFlow to manage the workout details
    private var _workoutState = WorkoutUiState()
    private var exercises: MutableList<RemainingExercise> = mutableListOf()
    private var currentExerciseIndex: Int = 0
    private var isPaused: Boolean = false

    // Timer state
    private val timerManager = TimerManager(viewModelScope)
    val exerciseSeconds: StateFlow<Int> get() = timerManager.seconds
    private var currentExerciseStartTime: String = ""

    // To track completed exercises during the workout
    private val completedExercises = mutableListOf<CompletedExercise>()
    private var sessionStartTime: String = ""
    private var sessionEndTime: String = ""
    private var lastCompletedExerciseIndex = -1
    
    // Timer and workout duration management
    private var workoutDurationMinutes: Int = 60 // Default, will be updated from actual workout
    private var expectedSecondsPerExercise: Int = 0

    fun startWorkout() {
        _uiState.value = PerformWorkoutUiState.Loading
        viewModelScope.launch {
            sessionStartTime = getCurrentTimeFormatted()
            exercises = loadExercises()
            val actualWorkout = loadWorkout()

            // Configurar duración esperada del workout y tiempo por ejercicio
            workoutDurationMinutes = actualWorkout?.durationMinutes ?: 60
            expectedSecondsPerExercise = if (exercises.isNotEmpty()) {
                (workoutDurationMinutes * 60) / exercises.size
            } else {
                300 // 5 minutos por defecto
            }

            val currentTime = java.time.LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            val formattedTime = currentTime.format(formatter)

            currentExerciseIndex = 0
            currentExerciseStartTime = getCurrentTimeHumanReadable()
            timerManager.reset()
            timerManager.start()

            val currentExercise = createCurrentExercise(
                exercises,
                currentExerciseIndex,
                formattedTime,
                exerciseSeconds.value
            )
            val nextExercise = createNextExercise(exercises, currentExerciseIndex + 1)
            val remainingExercises = exercises.drop(2)

            _workoutState = _workoutState.copy(
                title = actualWorkout?.name.orEmpty(),
                subtitle = actualWorkout?.type.toString(),
                progress = "1/${exercises.size} ejercicios completados",
                currentExercise = currentExercise,
                nextExercise = nextExercise,
                remainingExercises = remainingExercises
            )

            _uiState.value = PerformWorkoutUiState.Success(_workoutState)
            
            // Inicializar la actualización continua del timer
            startTimerUpdates()
        }
    }

    private fun startTimerUpdates() {
        viewModelScope.launch {
            exerciseSeconds.collect { seconds ->
                // Solo actualizar si estamos en un ejercicio válido y el estado actual necesita cambios
                if (currentExerciseIndex < exercises.size && _uiState.value is PerformWorkoutUiState.Success) {
                    val updatedCurrentExercise = createCurrentExercise(
                        exercises,
                        currentExerciseIndex,
                        currentExerciseStartTime,
                        seconds
                    )
                    
                    // Solo actualizar si el estado del tiempo ha cambiado (para evitar actualizaciones innecesarias)
                    val currentState = _workoutState.currentExercise
                    if (currentState.remainingTime != updatedCurrentExercise.remainingTime || 
                        currentState.isTimeExceeded != updatedCurrentExercise.isTimeExceeded) {
                        
                        _workoutState = _workoutState.copy(
                            currentExercise = updatedCurrentExercise
                        )
                        _uiState.value = PerformWorkoutUiState.Success(_workoutState)
                    }
                }
            }
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
                if (currentExerciseIndex > lastCompletedExerciseIndex) {
                    val exercise = exercises[currentExerciseIndex]
                    val kilosValues = _workoutState.currentExercise.kilosValues
                    val repsValues = _workoutState.currentExercise.repsValues
                    
                    // Calculate total reps: sum of all reps across all sets
                    val totalReps = repsValues.sum()
                    
                    // Calculate total weight: sum of (reps * weight) for each set
                    val totalVolume = repsValues.zip(kilosValues) { reps, weight -> reps * weight }.sum()
                    
                    // Get number of sets actually performed (non-zero reps)
                    val setsPerformed = repsValues.count { it > 0 }
                    
                    // Calculate average weight per rep for RPE approximation
                    val averageWeight = if (totalReps > 0) totalVolume / totalReps else 0f
                    
                    Log.d("PerformWorkoutViewModel", "Exercise completion - " +
                            "repsValues: $repsValues, " +
                            "kilosValues: $kilosValues, " +
                            "totalReps: $totalReps, " +
                            "totalVolume: $totalVolume, " +
                            "setsPerformed: $setsPerformed, " +
                            "averageWeight: $averageWeight")
                    
                    val completed = CompletedExercise(
                        id = null,
                        workoutSessionId = null,
                        exerciseId = exercise.id,
                        sets = setsPerformed, // Number of sets actually performed
                        reps = totalReps, // Total reps across all sets
                        rpe = averageWeight.toInt(), // Average weight as RPE approximation
                        weightKg = totalVolume.toInt() // Total volume (reps * weight across all sets)
                    )
                    completedExercises.add(completed)
                    lastCompletedExerciseIndex = currentExerciseIndex
                }
                currentExerciseIndex++
                currentExerciseStartTime = getCurrentTimeHumanReadable()
                timerManager.reset()
                timerManager.start()
                updateCurrentAndNextExercises()
            } else if (currentExerciseIndex == exercises.size - 1) {
                currentExerciseIndex++
                updateCurrentAndNextExercises()
            }
        }
    }

    fun goToPreviousExercise() {
        viewModelScope.launch {
            if (currentExerciseIndex > 0) {
                currentExerciseIndex--
                currentExerciseStartTime = getCurrentTimeHumanReadable()
                timerManager.reset()
                timerManager.start()
                updateCurrentAndNextExercises()
            }
        }
    }

    fun pauseExercise() {
        isPaused = !isPaused
        if (isPaused) {
            timerManager.pause()
        } else {
            timerManager.resume()
        }
    }

    fun skipToNextExercise() {
        goToNextExercise()
    }

    fun finishWorkout() {
        sessionEndTime = getCurrentTimeFormatted()
        viewModelScope.launch {
            val session = WorkoutSession(
                id = null,
                customerId = AppState.customerId.toString(),
                workoutId = workoutId,
                startTime = sessionStartTime,
                endTime = sessionEndTime,
                caloriesBurned = 0,
                distanceKm = null,
                averageHeartRate = 0
            )

            val result = workoutSessionUseCase.addWorkoutSession(
                workoutId = workoutId,
                session = session
            )
            result.onSuccess { sessionId ->
                completedExerciseUseCase.addCompletedExercises(
                    completedExercises.map { it.copy(workoutSessionId = sessionId) }
                )
            }
        }
        timerManager.stop()
        _uiState.value = PerformWorkoutUiState.Idle
    }

    private suspend fun loadExercises(): MutableList<RemainingExercise> {
        val workoutUUID = UUID.fromString(workoutId)
        val exercisesResponse = performWorkoutUseCase.getWorkoutExercises(workoutUUID)
        return exercisesResponse.getOrNull().orEmpty().map { item ->
            val exercise = exerciseUseCase.getExerciseById(item.exerciseId)
            // Siempre inicializar con 1 serie por defecto en lugar del valor de la BD
            val defaultSetsCount = 1
            val repsPerSet = item.reps
            val repsValues = List(defaultSetsCount) { repsPerSet }
            val kilosValues = List(defaultSetsCount) { 0f }
            RemainingExercise(
                id = item.exerciseId.toString(),
                title = exercise.name.toString(),
                sets = defaultSetsCount.toString(),
                reps = repsPerSet.toString(),
                repsValues = repsValues,
                kilosValues = kilosValues
            )
        }.toMutableList()
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
        val currentTime = java.time.LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        return currentTime.format(formatter)
    }

    private fun getCurrentTimeHumanReadable(): String {
        val currentTime = java.time.LocalDateTime.now()
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
            // Get the expected number of sets from the exercise definition
            val expectedSetsCount = it.sets.toIntOrNull() ?: 0
            
            // Preserve existing values and pad with defaults if needed
            val repsValues = if (it.repsValues.isNotEmpty()) {
                // Use existing values and pad if necessary
                it.repsValues.toMutableList().apply {
                    while (size < expectedSetsCount) add(0)
                }.take(expectedSetsCount)
            } else {
                // Create initial list with default values
                List(expectedSetsCount) { 0 }
            }
            
            val kilosValues = if (it.kilosValues.isNotEmpty()) {
                // Use existing values and pad if necessary
                it.kilosValues.toMutableList().apply {
                    while (size < expectedSetsCount) add(0f)
                }.take(expectedSetsCount)
            } else {
                // Create initial list with default values
                List(expectedSetsCount) { 0f }
            }
            
            val repsList = (1..expectedSetsCount).map { repNum -> "Set $repNum" }
            
            // Calcular si se ha excedido el tiempo esperado
            val isTimeExceeded = seconds > expectedSecondsPerExercise
            val remainingTimeText = if (isTimeExceeded) {
                val exceededSeconds = seconds - expectedSecondsPerExercise
                val exceededFormatted = formatSeconds(exceededSeconds)
                "Tiempo excedido: +$exceededFormatted"
            } else {
                val remainingSeconds = expectedSecondsPerExercise - seconds
                val remainingFormatted = formatSeconds(remainingSeconds)
                "Tiempo restante: $remainingFormatted"
            }
            
            Log.d("PerformWorkoutViewModel", "createCurrentExercise - ejercicio: ${it.title}, " +
                    "expectedSetsCount: $expectedSetsCount, " +
                    "repsValues: $repsValues, " +
                    "kilosValues: $kilosValues, " +
                    "seconds: $seconds, " +
                    "expectedSecondsPerExercise: $expectedSecondsPerExercise, " +
                    "isTimeExceeded: $isTimeExceeded")
            
            CurrentExercise(
                name = it.title,
                time = formattedTime,
                series = it.sets,
                remainingTime = remainingTimeText,
                repetitions = repsList,
                repsValues = repsValues,
                kilosValues = kilosValues,
                isTimeExceeded = isTimeExceeded
            )
        } ?: CurrentExercise()
    }

    private fun createNextExercise(
        exercises: List<RemainingExercise>,
        index: Int
    ): NextExercise {
        return exercises.getOrNull(index)?.let {
            NextExercise(it.title, it.sets.toInt(), it.reps.toInt())
        } ?: NextExercise(
            name = "No quedan más ejercicios 💪🔥",
            sets = 0,
            reps = 0
        )
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
        val current = createCurrentExercise(
            exercises,
            currentExerciseIndex,
            currentExerciseStartTime,
            exerciseSeconds.value
        )
        val next = createNextExercise(exercises, currentExerciseIndex + 1)
        val remaining = exercises.drop(currentExerciseIndex + 2)

        val progress = if (currentExerciseIndex < exercises.size) {
            "${currentExerciseIndex + 1}/${exercises.size} ejercicios completados"
        } else {
            "${exercises.size}/${exercises.size} ejercicios completados"
        }
        _workoutState = _workoutState.copy(
            currentExercise = current,
            nextExercise = next,
            remainingExercises = remaining,
            progress = progress
        )
        _uiState.value = PerformWorkoutUiState.Success(_workoutState)
    }

    private fun formatSeconds(seconds: Int): String {
        val min = seconds / 60
        val sec = seconds % 60
        return "%02d:%02d".format(min, sec)
    }

    fun recordCompletedExercise(exercise: CompletedExercise) {
        completedExercises.add(exercise)
    }

    fun hasUnfinishedExercises(): Boolean {
        // The number of completed exercises is the size of completedExercises
        // The total number of exercises is exercises.size
        // If there are any exercises not completed, return true
        return completedExercises.size < exercises.size
    }

    fun updateRepsValues(newValues: List<Int>) {
        Log.d("PerformWorkoutViewModel", "updateRepsValues llamado con valores: $newValues")
        
        // Actualizar también los valores en la lista de ejercicios para que persistan
        if (currentExerciseIndex < exercises.size) {
            exercises[currentExerciseIndex] = exercises[currentExerciseIndex].copy(
                repsValues = newValues
            )
        }
        
        // Actualiza los valores de repeticiones en el ejercicio actual
        _workoutState = _workoutState.copy(
            currentExercise = _workoutState.currentExercise.copy(
                repsValues = newValues
            )
        )
        Log.d(
            "PerformWorkoutViewModel",
            "Después de updateRepsValues, currentExercise.repsValues: ${_workoutState.currentExercise.repsValues}"
        )
        _uiState.value = PerformWorkoutUiState.Success(_workoutState)
    }

    fun updateKilosValues(newValues: List<Float>) {
        Log.d("PerformWorkoutViewModel", "updateKilosValues llamado con valores: $newValues")
        
        // Actualizar también los valores en la lista de ejercicios para que persistan
        if (currentExerciseIndex < exercises.size) {
            exercises[currentExerciseIndex] = exercises[currentExerciseIndex].copy(
                kilosValues = newValues
            )
        }
        
        // Actualiza los valores de kilos en el ejercicio actual
        _workoutState = _workoutState.copy(
            currentExercise = _workoutState.currentExercise.copy(
                kilosValues = newValues
            )
        )
        Log.d(
            "PerformWorkoutViewModel",
            "Después de updateKilosValues, currentExercise.kilosValues: ${_workoutState.currentExercise.kilosValues}"
        )
        _uiState.value = PerformWorkoutUiState.Success(_workoutState)
    }

    fun updateSetsCount(newSetsCount: Int) {
        Log.d("PerformWorkoutViewModel", "updateSetsCount llamado con nuevo valor: $newSetsCount")
        
        if (currentExerciseIndex < exercises.size) {
            val currentExercise = exercises[currentExerciseIndex]
            
            // Actualizar el número de series en el ejercicio
            exercises[currentExerciseIndex] = currentExercise.copy(
                sets = newSetsCount.toString(),
                repsValues = adjustListSize(currentExercise.repsValues, newSetsCount, currentExercise.reps.toIntOrNull() ?: 1),
                kilosValues = adjustListSize(currentExercise.kilosValues, newSetsCount, 0f)
            )
            
            // Actualizar también el estado actual de la UI
            val updatedRepsList = (1..newSetsCount).map { "Set $it" }
            _workoutState = _workoutState.copy(
                currentExercise = _workoutState.currentExercise.copy(
                    series = newSetsCount.toString(),
                    repetitions = updatedRepsList
                )
            )
            
            Log.d("PerformWorkoutViewModel", 
                "Después de updateSetsCount, exercise.sets: ${exercises[currentExerciseIndex].sets}, " +
                "repsValues.size: ${exercises[currentExerciseIndex].repsValues.size}, " +
                "kilosValues.size: ${exercises[currentExerciseIndex].kilosValues.size}")
                
            _uiState.value = PerformWorkoutUiState.Success(_workoutState)
        }
    }
    
    private fun <T> adjustListSize(currentList: List<T>, targetSize: Int, defaultValue: T): List<T> {
        return when {
            currentList.size == targetSize -> currentList
            currentList.size < targetSize -> currentList + List(targetSize - currentList.size) { defaultValue }
            else -> currentList.take(targetSize)
        }
    }
}