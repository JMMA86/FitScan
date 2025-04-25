package icesi.edu.co.fitscan.features.auth.ui.model

sealed interface LoginUiState {
    object Idle : LoginUiState
    object Loading : LoginUiState
    data class Success(val accessToken: String) : LoginUiState
    data class Error(val message: String) : LoginUiState
}