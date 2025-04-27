package icesi.edu.co.fitscan.features.auth.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import icesi.edu.co.fitscan.features.auth.data.remote.RetrofitInstance
import icesi.edu.co.fitscan.features.auth.domain.service.AuthServiceImpl
import icesi.edu.co.fitscan.features.auth.domain.usecase.RegisterUseCase
import icesi.edu.co.fitscan.features.auth.ui.model.RegisterUiState
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

        // Save user profile data to shared preferences for later use
        saveUserProfileData(email, age, phone)

        _uiState.update { RegisterUiState.Loading }

        viewModelScope.launch {
            val registerResult = registerUseCase(email, password, firstName, lastName)

            registerResult.fold(
                onSuccess = {
                    _uiState.update { RegisterUiState.Success }
                },
                onFailure = { exception ->
                    handleError(exception)
                }
            )
        }
    }

    private fun saveUserProfileData(email: String, age: String, phone: String) {
        val context = getApplication<Application>()
        val sharedPref = context.getSharedPreferences("profile_data", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("PENDING_USER_ID", email)
            putString("PENDING_USER_AGE", age)
            putString("PENDING_USER_PHONE", phone)
            putBoolean("NEEDS_CUSTOMER_CREATION", true)
            apply()
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