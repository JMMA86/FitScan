package icesi.edu.co.fitscan.features.auth.ui.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import icesi.edu.co.fitscan.domain.model.EstimatedBodyMeasurements
import icesi.edu.co.fitscan.domain.usecases.IEstimateBodyMeasurementsUseCase
import icesi.edu.co.fitscan.features.auth.data.remote.request.BodyMeasure
import icesi.edu.co.fitscan.features.auth.domain.service.AuthServiceImpl
import icesi.edu.co.fitscan.features.auth.ui.model.BodyMeasureUiState
import icesi.edu.co.fitscan.features.common.data.remote.RetrofitInstance
import icesi.edu.co.fitscan.features.openai.data.repositories.IOpenAiRepositoryImpl
import icesi.edu.co.fitscan.features.openai.data.usecases.EstimateBodyMeasurementsUseCaseImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


class BodyMeasurementViewModel(application: Application) : AndroidViewModel(application) {
    private val authService = AuthServiceImpl(RetrofitInstance.authRepository, application)
    
    // AI estimation dependencies
    private val openAiRepository = IOpenAiRepositoryImpl(application)
    private val estimateBodyMeasurementsUseCase = EstimateBodyMeasurementsUseCaseImpl(openAiRepository)

    private val _uiState = MutableStateFlow<BodyMeasureUiState>(BodyMeasureUiState.Idle)
    val uiState: StateFlow<BodyMeasureUiState> = _uiState.asStateFlow()
    
    // State for AI estimation
    private val _isEstimating = MutableStateFlow(false)
    val isEstimating: StateFlow<Boolean> = _isEstimating.asStateFlow()
    
    private val _estimatedMeasurements = MutableStateFlow<EstimatedBodyMeasurements?>(null)
    val estimatedMeasurements: StateFlow<EstimatedBodyMeasurements?> = _estimatedMeasurements.asStateFlow()

    fun saveMeasurements(
        height: Double,
        weight: Double,
        arms: Double,
        chest: Double,
        waist: Double,
        hips: Double,
        thighs: Double,
        calves: Double
    ) {
        if (_uiState.value is BodyMeasureUiState.Loading) return

        _uiState.update { BodyMeasureUiState.Loading }

        viewModelScope.launch {
            try {
                val bodyMeasure = BodyMeasure(
                    height_cm = height,
                    weight_kg = weight,
                    arms_cm = arms,
                    chest_cm = chest,
                    waist_cm = waist,
                    hips_cm = hips,
                    thighs_cm = thighs,
                    calves_cm = calves
                )

                val result = authService.saveBodyMeasurements(bodyMeasure)

                result.fold(
                    onSuccess = { _: Unit ->
                        Log.d("BodyMeasurementVM", "Body measurements saved successfully")
                        _uiState.update { BodyMeasureUiState.Success }
                    },
                    onFailure = { exception: Throwable ->
                        Log.e(
                            "BodyMeasurementVM",
                            "Failed to save measurements: ${exception.message}"
                        )
                        _uiState.update { BodyMeasureUiState.Error(getErrorMessage(exception)) }
                    }
                )
            } catch (e: Exception) {
                _uiState.update { BodyMeasureUiState.Error(getErrorMessage(e)) }
            }
        }
    }

    private fun getErrorMessage(exception: Throwable): String {
        return when (exception) {
            is HttpException -> "Error del servidor: ${exception.code()}"
            is IOException -> "Error de conexión. Verifica tu internet."
            else -> "Error: ${exception.message}"
        }
    }

    fun resetState() {
        _uiState.update { BodyMeasureUiState.Idle }
    }

    private fun getBodyMeasurementsByBodyMeasureId(bodyMeasureId: String) {
        _uiState.update { BodyMeasureUiState.Loading }
        viewModelScope.launch {
            val result = authService.getBodyMeasureById(bodyMeasureId)
            result.fold(
                onSuccess = { data ->
                    _uiState.update { BodyMeasureUiState.SuccessData(data) }
                },
                onFailure = { exception ->
                    _uiState.update { BodyMeasureUiState.Error(exception.message ?: "Error desconocido") }
                }
            )
        }
    }

    fun getBodyMeasurementsByCustomerId(userId: String) {
        _uiState.update { BodyMeasureUiState.Loading }
        viewModelScope.launch {
            val customerResult = authService.getCustomerByCustomerId(userId)
            customerResult.fold(
                onSuccess = { customerData ->
                    val bodyMeasureId = customerData.body_measure_id
                    if (bodyMeasureId.isNotEmpty()) {
                        getBodyMeasurementsByBodyMeasureId(bodyMeasureId)
                    } else {
                        _uiState.update { BodyMeasureUiState.Error("No se encontró body_measure_id para el usuario.") }
                    }
                },
                onFailure = { exception ->
                    _uiState.update { BodyMeasureUiState.Error(exception.message ?: "Error desconocido") }
                }
            )
        }
    }
      fun estimateMeasurementsFromImage(
        imageUri: Uri,
        height: Double,
        weight: Double,
        existingMeasurements: Map<String, Double>
    ) {
        if (_isEstimating.value) return
        
        _isEstimating.value = true
        _estimatedMeasurements.value = null  // Clear previous estimates
        
        viewModelScope.launch {
            try {
                val result = estimateBodyMeasurementsUseCase(
                    context = getApplication(),
                    imageUri = imageUri,
                    height = height,
                    weight = weight,
                    existingMeasurements = existingMeasurements
                )
                
                result.fold(
                    onSuccess = { estimatedMeasurements ->
                        _estimatedMeasurements.value = estimatedMeasurements
                        Log.d("BodyMeasurementVM", "AI estimation successful: $estimatedMeasurements")
                        // Clear any previous error state
                        if (_uiState.value is BodyMeasureUiState.Error) {
                            _uiState.update { BodyMeasureUiState.Idle }
                        }
                    },
                    onFailure = { exception ->
                        Log.e("BodyMeasurementVM", "AI estimation failed: ${exception.message}")
                        val errorMessage = exception.message ?: "Error desconocido en estimación AI"
                        _uiState.update { BodyMeasureUiState.Error(errorMessage) }
                        
                        // Auto-clear error after 5 seconds
                        launch {
                            kotlinx.coroutines.delay(5000)
                            if (_uiState.value is BodyMeasureUiState.Error) {
                                _uiState.update { BodyMeasureUiState.Idle }
                            }
                        }
                    }
                )
            } catch (e: Exception) {
                Log.e("BodyMeasurementVM", "Unexpected error in AI estimation: ${e.message}")
                _uiState.update { BodyMeasureUiState.Error("Error inesperado: ${e.message}") }
            } finally {
                _isEstimating.value = false
            }
        }
    }
    
    fun clearEstimatedMeasurements() {
        _estimatedMeasurements.value = null
    }
}