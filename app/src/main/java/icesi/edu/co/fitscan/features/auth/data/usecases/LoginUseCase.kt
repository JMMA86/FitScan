package icesi.edu.co.fitscan.features.auth.data.usecases

import icesi.edu.co.fitscan.features.auth.data.dto.LoginResponseData
import icesi.edu.co.fitscan.features.auth.data.repositories.AuthRepository

class LoginUseCase(private val authService: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<LoginResponseData> {

        if (email.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("Email y contraseña no pueden estar vacíos"))
        }
        return authService.login(email, password)
    }
}