package icesi.edu.co.fitscan.features.auth.domain.usecase

import icesi.edu.co.fitscan.features.auth.domain.repository.AuthService

class RegisterUseCase(private val authService: AuthService) {
    suspend operator fun invoke(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ): Result<Unit> {

        // Validate inputs
        if (email.isBlank()) {
            return Result.failure(IllegalArgumentException("Email no puede estar vacío"))
        }

        if (password.isBlank()) {
            return Result.failure(IllegalArgumentException("Contraseña no puede estar vacía"))
        }

        if (firstName.isBlank()) {
            return Result.failure(IllegalArgumentException("Nombre no puede estar vacío"))
        }

        if (lastName.isBlank()) {
            return Result.failure(IllegalArgumentException("Apellido no puede estar vacío"))
        }

        // Delegate to repository
        return authService.register(email, password, firstName, lastName)
    }
}