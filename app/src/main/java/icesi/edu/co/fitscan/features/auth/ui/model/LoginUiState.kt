// app/src/main/java/icesi/edu/co/fitscan/features/auth/ui/model/LoginUiState.kt
package icesi.edu.co.fitscan.features.auth.ui.model

sealed interface LoginUiState {
    object Idle : LoginUiState
    object Loading : LoginUiState
    data class Success(val data: Unit = Unit) : LoginUiState
    data class Error(val message: String) : LoginUiState
}