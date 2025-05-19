package icesi.edu.co.fitscan.features.auth.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import icesi.edu.co.fitscan.features.auth.domain.service.AuthServiceImpl
import icesi.edu.co.fitscan.features.auth.domain.usecase.LoginUseCase
import icesi.edu.co.fitscan.features.auth.ui.model.LoginUiState
import icesi.edu.co.fitscan.features.common.data.remote.RetrofitInstance
import icesi.edu.co.fitscan.features.common.ui.viewmodel.AppState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val authService = AuthServiceImpl(RetrofitInstance.authRepository, application)
    private val loginUseCase = LoginUseCase(authService)

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        if (_uiState.value is LoginUiState.Loading) return

        if (email.isBlank() || password.isBlank()) {
            _uiState.value = LoginUiState.Error("Email y contraseña son requeridos")
            return
        }

        _uiState.value = LoginUiState.Loading

        viewModelScope.launch {
            val result = loginUseCase(email, password)

            result.fold(
                onSuccess = { loginData ->
                    AppState.token = loginData.access_token
                    val userId = extractUserIdFromToken(loginData.access_token)
                    if (userId != null) {
                        AppState.customerId = userId
                        _uiState.value = LoginUiState.Success()
                    } else {
                        _uiState.value = LoginUiState.Error("Error al procesar los datos de usuario")
                    }
                },
                onFailure = { exception ->
                    handleError(exception)
                }
            )
        }
    }

    private fun extractUserIdFromToken(token: String): String? {
        return try {
            val parts = token.split(".")
            if (parts.size < 2) return null
            val payload = String(android.util.Base64.decode(parts[1], android.util.Base64.DEFAULT))
            val json = org.json.JSONObject(payload)
            json.getString("id")
        } catch (e: Exception) {
            null
        }
    }

    private fun handleError(exception: Throwable) {
        val errorMessage = when (exception) {
            is HttpException -> {
                when (exception.code()) {
                    401 -> "Credenciales incorrectas"
                    else -> "Error del servidor (${exception.code()})"
                }
            }

            is IOException -> "Error de red. Verifica tu conexión."
            else -> "Error inesperado: ${exception.message}"
        }
        _uiState.value = LoginUiState.Error(errorMessage)
    }

    fun resetState() {
        _uiState.value = LoginUiState.Idle
    }
}