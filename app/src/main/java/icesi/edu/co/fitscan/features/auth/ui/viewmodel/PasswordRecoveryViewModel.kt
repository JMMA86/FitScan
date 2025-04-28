package icesi.edu.co.fitscan.features.auth.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PasswordRecoveryViewModel : ViewModel() {
    sealed class PasswordRecoveryUiState {
        object Idle : PasswordRecoveryUiState()
        object Loading : PasswordRecoveryUiState()
        object Success : PasswordRecoveryUiState()
        data class Error(val message: String) : PasswordRecoveryUiState()
    }

    private val _uiState = MutableStateFlow<PasswordRecoveryUiState>(PasswordRecoveryUiState.Idle)
    val uiState: StateFlow<PasswordRecoveryUiState> = _uiState

    fun sendRecoveryCode(email: String) {
        viewModelScope.launch {
            _uiState.value = PasswordRecoveryUiState.Loading
            // Aquí iría la lógica real para enviar el código
            // Simulamos una respuesta después de 1 segundo
            kotlinx.coroutines.delay(1000)

            if (email.contains("@")) {
                _uiState.value = PasswordRecoveryUiState.Success
            } else {
                _uiState.value = PasswordRecoveryUiState.Error("Correo electrónico inválido")
            }
        }
    }

    fun resetState() {
        _uiState.value = PasswordRecoveryUiState.Idle
    }
}