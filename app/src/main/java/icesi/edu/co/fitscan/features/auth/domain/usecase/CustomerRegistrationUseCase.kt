package icesi.edu.co.fitscan.features.auth.domain.usecase

import icesi.edu.co.fitscan.features.auth.data.remote.request.Customer
import icesi.edu.co.fitscan.features.auth.domain.service.AuthService

class CustomerRegistrationUseCase(private val authService: AuthService) {
    suspend operator fun invoke(
        userId: String, // Now expecting UUID, not email
        age: Int,
        phoneNumber: String
    ): Result<Unit> {
        try {
            val customer = Customer(user_id = userId, age = age, phone = phoneNumber)
            return authService.registerCustomer(customer)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}