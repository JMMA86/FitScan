package icesi.edu.co.fitscan.features.auth.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import icesi.edu.co.fitscan.features.auth.data.remote.RetrofitInstance
import icesi.edu.co.fitscan.features.auth.domain.repository.AuthRepositoryImpl
import icesi.edu.co.fitscan.features.auth.domain.usecase.LoginUseCase
import icesi.edu.co.fitscan.features.auth.ui.model.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class LoginViewModel : ViewModel() {
    private val authRepository = AuthRepositoryImpl(RetrofitInstance.authService)
    private val loginUseCase = LoginUseCase(authRepository)
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        if (_uiState.value == LoginUiState.Loading) return
        viewModelScope.launch {
            _uiState.update { LoginUiState.Loading }
            val result = loginUseCase(email, password)

            result.fold(
                onSuccess = { loginResponseData ->
                    _uiState.update { LoginUiState.Success(loginResponseData.access_token) }
                },
                onFailure = { exception ->
                    val errorMessage = when (exception) {
                        is HttpException -> {

                            // Manejar errores HTTP específicos o cuando hagamos los goooooogle
                            when (exception.code()) {
                                401 -> "Credenciales inválidas."
                                400 -> "Solicitud incorrecta. Verifica los datos."
                                //O pues uno general del server ahi miramos despues
                                else -> "Error del servidor (${exception.code()})."
                            }
                        }
                        is IOException -> "Error de red. Verifica tu conexión."
                        is IllegalArgumentException -> exception.message ?: "Datos inválidos."
                        else -> "Error inesperado: ${exception.localizedMessage}"
                    }
                    _uiState.update { LoginUiState.Error(errorMessage) }
                }
            )
        }
    }

    fun resetState() {
        _uiState.update { LoginUiState.Idle }
    }
}