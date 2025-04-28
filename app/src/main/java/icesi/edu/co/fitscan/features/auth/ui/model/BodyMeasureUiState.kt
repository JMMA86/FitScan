package icesi.edu.co.fitscan.features.auth.ui.model

sealed interface BodyMeasureUiState {
    object Idle : BodyMeasureUiState
    object Loading : BodyMeasureUiState
    object Success : BodyMeasureUiState
    data class Error(val message: String) : BodyMeasureUiState
}