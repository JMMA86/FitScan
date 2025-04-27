package icesi.edu.co.fitscan.features.auth.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel // Cambiado de ViewModel
import androidx.lifecycle.viewModelScope
import icesi.edu.co.fitscan.features.auth.data.remote.RetrofitInstance
import icesi.edu.co.fitscan.features.auth.domain.repository.AuthServiceImpl
import icesi.edu.co.fitscan.features.auth.domain.usecase.LoginUseCase
import icesi.edu.co.fitscan.features.auth.ui.model.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val authRepository = AuthServiceImpl(RetrofitInstance.authRepository)
    private val loginUseCase = LoginUseCase(authRepository)

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        if (_uiState.value == LoginUiState.Loading) return

        _uiState.update { LoginUiState.Loading }

        viewModelScope.launch {

            val result = loginUseCase(email, password)

            result.fold(
                onSuccess = { loginResponseData ->
                    saveAuthToken(loginResponseData.accessToken)
                    _uiState.update { LoginUiState.Success(loginResponseData.accessToken) }
                },
                onFailure = { exception ->
                    val errorMessage = when (exception) {
                        is HttpException -> {
                            when (exception.code()) {
                                401 -> "Credenciales inválidas."
                                400 -> "Solicitud incorrecta. Verifica los datos."
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

    private fun saveAuthToken(token: String) {
        val context = getApplication<Application>().applicationContext
        val sharedPref = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("AUTH_TOKEN", token)
            apply()
        }
        //Log.d("LoginViewModel", "Token guardado exitosamente")
    }

    fun getAuthToken(): String? {
        val context = getApplication<Application>().applicationContext
        val sharedPref = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        return sharedPref.getString("AUTH_TOKEN", null)
    }

    fun resetState() {
        _uiState.update { LoginUiState.Idle }
    }
}