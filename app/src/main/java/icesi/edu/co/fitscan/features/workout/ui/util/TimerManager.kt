package icesi.edu.co.fitscan.features.workout.ui.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TimerManager(private val scope: CoroutineScope) {
    private var timerJob: Job? = null
    private val _seconds = MutableStateFlow(0)
    val seconds: StateFlow<Int> get() = _seconds
    private var isPaused = false

    fun start() {
        timerJob?.cancel()
        isPaused = false
        timerJob = scope.launch {
            while (true) {
                delay(1000)
                if (!isPaused) {
                    _seconds.value += 1
                }
            }
        }
    }

    fun pause() {
        isPaused = true
    }

    fun resume() {
        isPaused = false
    }

    fun reset() {
        _seconds.value = 0
    }

    fun stop() {
        timerJob?.cancel()
        _seconds.value = 0
    }
}
