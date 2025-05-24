package icesi.edu.co.fitscan.features.auth.data.usecases

import icesi.edu.co.fitscan.features.auth.data.repositories.AuthRepository

class RegisterUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
    ): Result<Unit> {
        return try {
            authRepository.register(email, password, firstName, lastName)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}