package icesi.edu.co.fitscan.features.auth.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import icesi.edu.co.fitscan.features.auth.domain.service.AuthServiceImpl
import icesi.edu.co.fitscan.features.auth.domain.usecase.CustomerRegistrationUseCase
import icesi.edu.co.fitscan.features.auth.domain.usecase.LoginUseCase
import icesi.edu.co.fitscan.features.auth.domain.usecase.RegisterUseCase
import icesi.edu.co.fitscan.features.auth.ui.model.RegisterUiState
import icesi.edu.co.fitscan.features.common.data.remote.RetrofitInstance
import icesi.edu.co.fitscan.notification.NotificationUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val authService = AuthServiceImpl(RetrofitInstance.authRepository, application)
    private val registerUseCase = RegisterUseCase(authService)
    private val loginUseCase = LoginUseCase(authService)
    private val customerRegistrationUseCase = CustomerRegistrationUseCase(authService)

    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun register(
        email: String,
        password: String,
        fullName: String,
        age: String,
        phone: String,
        acceptedTerms: Boolean
    ) {
        if (_uiState.value == RegisterUiState.Loading) return

        if (!acceptedTerms) {
            _uiState.update { RegisterUiState.Error("Debes aceptar los términos y condiciones") }
            return
        }

        // Validations
        if (email.isBlank() || password.isBlank() || fullName.isBlank() || age.isBlank() || phone.isBlank()) {
            _uiState.update { RegisterUiState.Error("Todos los campos son requeridos") }
            return
        }

        // Parse fullName into firstName and lastName
        val nameParts = fullName.trim().split(" ").filter { it.isNotEmpty() }
        if (nameParts.isEmpty()) {
            _uiState.update { RegisterUiState.Error("Nombre inválido") }
            return
        }

        val firstName = nameParts[0]
        val lastName =
            if (nameParts.size > 1) nameParts.subList(1, nameParts.size)
                .joinToString(" ") else "N/A"

        try {
            age.toInt()  // Just validate age is a number
        } catch (e: NumberFormatException) {
            _uiState.update { RegisterUiState.Error("Edad inválida") }
            return
        }

        _uiState.update { RegisterUiState.Loading }

        viewModelScope.launch {
            val registerResult = registerUseCase(email, password, firstName, lastName)

            registerResult.fold(
                onSuccess = {
                    val loginResult = loginUseCase(email, password)

                    loginResult.fold(
                        onSuccess = { loginData ->
                            val userId = extractUserIdFromToken(loginData.access_token)

                            if (userId != null) {
                                val customerResult = customerRegistrationUseCase(
                                    userId,
                                    age.toInt(),
                                    phone
                                )

                                customerResult.fold(
                                    onSuccess = {
                                        Log.d(
                                            "RegisterViewModel",
                                            "Registration completed successfully"
                                        )
                                        _uiState.update { RegisterUiState.Success }

                                        // Send notification
                                        NotificationUtil.showNotification(getApplication(), "Registro Exitoso 😎", "¡Bienvenido a FitScan! ¿Listo para transformar tu vida? 🤑🔥")
                                    },
                                    onFailure = { customerException ->
                                        Log.e(
                                            "RegisterViewModel",
                                            "Customer creation failed: ${customerException.message}"
                                        )
                                        _uiState.update { RegisterUiState.Error("El usuario ya está registrado") }
                                    }
                                )
                            } else {
                                Log.e("RegisterViewModel", "Failed to extract user ID from token")
                                _uiState.update { RegisterUiState.Error("Error al procesar los datos de usuario") }
                            }
                        },
                        onFailure = { loginException ->
                            Log.e(
                                "RegisterViewModel",
                                "Auto-login failed: ${loginException.message}"
                            )
                            _uiState.update { RegisterUiState.Error("Error al iniciar sesión: ${loginException.message}") }
                        }
                    )
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
            json.getString("id") // Make sure this matches the claim name in your JWT
        } catch (e: Exception) {
            Log.e("RegisterViewModel", "Error decoding token: ${e.message}")
            null
        }
    }

    private fun handleError(exception: Throwable) {
        val errorMessage = when (exception) {
            is HttpException -> {
                when (exception.code()) {
                    409 -> "El correo ya está registrado."
                    400 -> "Datos de registro incorrectos."
                    403 -> "No tienes permisos para realizar esta acción."
                    else -> "Error del servidor (${exception.code()})."
                }
            }

            is IOException -> "Error de red. Verifica tu conexión."
            is IllegalArgumentException -> exception.message ?: "Datos inválidos."
            else -> "Error inesperado: ${exception.localizedMessage}"
        }
        _uiState.update { RegisterUiState.Error(errorMessage) }
    }

    fun resetState() {
        _uiState.update { RegisterUiState.Idle }
    }
}