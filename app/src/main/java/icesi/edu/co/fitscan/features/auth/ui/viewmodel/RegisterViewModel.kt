package icesi.edu.co.fitscan.features.auth.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import icesi.edu.co.fitscan.features.auth.data.remote.RetrofitInstance
import icesi.edu.co.fitscan.features.auth.data.remote.response.RegisterResponse
import icesi.edu.co.fitscan.features.auth.domain.repository.AuthRepositoryImpl
import kotlinx.coroutines.launch

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val authRepository = AuthRepositoryImpl(RetrofitInstance.authService)

    fun register(email: String, password: String, firstName: String, lastName: String, onResult: (Result<RegisterResponse>) -> Unit) {
        viewModelScope.launch {
            val result = authRepository.register(email, password, firstName, lastName)
        }
    }
}