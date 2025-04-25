package icesi.edu.co.fitscan.features.auth.domain.usecase
import icesi.edu.co.fitscan.features.auth.domain.repository.AuthRepository
import icesi.edu.co.fitscan.features.auth.data.remote.response.LoginResponseData

class LoginUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<LoginResponseData> {

        if (email.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("Email y contraseña no pueden estar vacíos"))
        }
        return authRepository.login(email, password)
    }
}