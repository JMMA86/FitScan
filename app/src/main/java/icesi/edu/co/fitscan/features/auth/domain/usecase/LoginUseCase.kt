package icesi.edu.co.fitscan.features.auth.domain.usecase

import icesi.edu.co.fitscan.features.auth.data.remote.response.LoginResponseData
import icesi.edu.co.fitscan.features.auth.domain.service.AuthService

class LoginUseCase(private val authService: AuthService) {
    suspend operator fun invoke(email: String, password: String): Result<LoginResponseData> {

        if (email.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("Email y contraseña no pueden estar vacíos"))
        }
        return authService.login(email, password)
    }
}