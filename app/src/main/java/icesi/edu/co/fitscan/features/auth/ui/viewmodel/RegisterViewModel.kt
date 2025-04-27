package icesi.edu.co.fitscan.features.auth.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import icesi.edu.co.fitscan.features.auth.data.remote.RetrofitInstance
import icesi.edu.co.fitscan.features.auth.domain.repository.AuthServiceImpl
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
    private val authRepository = AuthServiceImpl(RetrofitInstance.authRepository)
    private val registerUseCase = RegisterUseCase(authRepository)

    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun register(email: String, password: String, firstName: String, lastName: String) {
        if (_uiState.value == RegisterUiState.Loading) return

        _uiState.update { RegisterUiState.Loading }

        viewModelScope.launch {
            val result = registerUseCase(email, password, firstName, lastName)

            result.fold(
                onSuccess = {
                    _uiState.update { RegisterUiState.Success }
                },
                onFailure = { exception ->
                    val errorMessage = when (exception) {
                        is HttpException -> {
                            when (exception.code()) {
                                409 -> "El correo ya está registrado."
                                400 -> "Datos de registro incorrectos."
                                else -> "Error del servidor (${exception.code()})."
                            }
                        }

                        is IOException -> "Error de red. Verifica tu conexión."
                        is IllegalArgumentException -> exception.message ?: "Datos inválidos."
                        else -> "Error inesperado: ${exception.localizedMessage}"
                    }
                    _uiState.update { RegisterUiState.Error(errorMessage) }
                }
            )
        }
    }

    fun resetState() {
        _uiState.update { RegisterUiState.Idle }
    }
}