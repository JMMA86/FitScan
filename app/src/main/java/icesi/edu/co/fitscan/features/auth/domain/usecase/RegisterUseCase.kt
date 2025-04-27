package icesi.edu.co.fitscan.features.auth.domain.usecase

import icesi.edu.co.fitscan.features.auth.domain.service.AuthService

class RegisterUseCase(private val authService: AuthService) {
    suspend operator fun invoke(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
    ): Result<Unit> {
        return try {
            authService.register(email, password, firstName, lastName)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}