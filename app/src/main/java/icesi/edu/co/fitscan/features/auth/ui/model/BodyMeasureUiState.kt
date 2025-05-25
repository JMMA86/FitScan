package icesi.edu.co.fitscan.features.auth.ui.model

import icesi.edu.co.fitscan.features.auth.data.remote.response.BodyMeasureData

sealed interface BodyMeasureUiState {
    object Idle : BodyMeasureUiState
    object Loading : BodyMeasureUiState
    object Success : BodyMeasureUiState
    data class SuccessData(val data: BodyMeasureData) : BodyMeasureUiState
    data class Error(val message: String) : BodyMeasureUiState
}