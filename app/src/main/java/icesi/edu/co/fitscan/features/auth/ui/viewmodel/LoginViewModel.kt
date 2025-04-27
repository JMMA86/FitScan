package icesi.edu.co.fitscan.features.auth.ui.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import icesi.edu.co.fitscan.features.auth.data.remote.RetrofitInstance
import icesi.edu.co.fitscan.features.auth.domain.service.AuthServiceImpl
import icesi.edu.co.fitscan.features.auth.domain.usecase.CustomerRegistrationUseCase
import icesi.edu.co.fitscan.features.auth.domain.usecase.LoginUseCase
import icesi.edu.co.fitscan.features.auth.ui.model.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


// Fix LoginViewModel.kt
class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val authService = AuthServiceImpl(RetrofitInstance.authRepository, application)
    private val loginUseCase = LoginUseCase(authService)
    private val customerRegistrationUseCase = CustomerRegistrationUseCase(authService)

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
                    // Check if we need to create customer profile
                    checkAndCreateCustomerProfile()
                },
                onFailure = { exception ->
                    handleError(exception)
                }
            )
        }
    }

    private suspend fun checkAndCreateCustomerProfile() {
        val context = getApplication<Application>()
        val sharedPref = context.getSharedPreferences("profile_data", Context.MODE_PRIVATE)

        val needsCustomerCreation = sharedPref.getBoolean("NEEDS_CUSTOMER_CREATION", false)

        if (needsCustomerCreation) {
            val userId = sharedPref.getString("PENDING_USER_ID", null)
            val age = sharedPref.getString("PENDING_USER_AGE", null)
            val phone = sharedPref.getString("PENDING_USER_PHONE", null)

            if (userId != null && age != null && phone != null) {
                val customerResult = customerRegistrationUseCase(userId, age.toInt(), phone)

                customerResult.fold(
                    onSuccess = {
                        // Clear the pending flag
                        with(sharedPref.edit()) {
                            putBoolean("NEEDS_CUSTOMER_CREATION", false)
                            apply()
                        }
                        _uiState.value = LoginUiState.Success()
                    },
                    onFailure = { exception ->
                        // We succeeded at login but failed at customer creation
                        // Still report success but log the error
                        Log.e("LoginViewModel", "Failed to create customer: ${exception.message}")
                        _uiState.value = LoginUiState.Success()
                    }
                )
            } else {
                _uiState.value = LoginUiState.Success()
            }
        } else {
            _uiState.value = LoginUiState.Success()
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