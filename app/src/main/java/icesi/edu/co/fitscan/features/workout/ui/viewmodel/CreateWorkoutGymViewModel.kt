package icesi.edu.co.fitscan.features.workout.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import icesi.edu.co.fitscan.features.workout.domain.model.Exercise
import icesi.edu.co.fitscan.features.workout.domain.usecase.GetExercisesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CreateWorkoutGymViewModel(
    private val getExercisesUseCase: GetExercisesUseCase
) : ViewModel() {

    private val _exercises = MutableStateFlow<List<Exercise>>(emptyList())
    val exercises: StateFlow<List<Exercise>> = _exercises

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _requestDetails = MutableStateFlow<String?>(null)

    init {
        loadExercises()
    }
    
    private fun loadExercises() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _requestDetails.value = null  // Limpiar detalles anteriores
            
            try {
                getExercisesUseCase()
                    .catch { e ->
                        val errorMessage = when {
                            e.message?.contains("403") == true -> 
                                "Error de autorización (403). Verifica que tienes un token válido."
                            e.message?.contains("Unable to resolve host") == true -> 
                                "No se pudo conectar al servidor. Verifica tu conexión a Internet."
                            e.message?.contains("timeout") == true -> 
                                "Tiempo de espera agotado. El servidor está tardando en responder."
                            else -> e.message ?: "Error desconocido al cargar ejercicios"
                        }
                        _error.value = errorMessage
                        
                        // Capturar el mensaje completo del error para análisis
                        _requestDetails.value = "Error completo: ${e.message}\n" +
                                              "Causa: ${e.cause}\n" +
                                              "Stack trace: ${e.stackTraceToString()}"
                        
                        _isLoading.value = false
                    }
                    .collect { exercises ->
                        _exercises.value = exercises
                        _isLoading.value = false
                    }
            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("403") == true -> 
                        "Error de autorización (403). Verifica que tienes un token válido."
                    e.message?.contains("Unable to resolve host") == true -> 
                        "No se pudo conectar al servidor. Verifica tu conexión a Internet."
                    e.message?.contains("timeout") == true -> 
                        "Tiempo de espera agotado. El servidor está tardando en responder."
                    else -> e.message ?: "Error desconocido al cargar ejercicios"
                }
                _error.value = errorMessage
                
                // Capturar el mensaje completo del error para análisis
                _requestDetails.value = "Error completo: ${e.message}\n" +
                                      "Causa: ${e.cause}\n" +
                                      "Stack trace: ${e.stackTraceToString()}"
                
                _isLoading.value = false
            }
        }
    }
}