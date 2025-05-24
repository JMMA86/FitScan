package icesi.edu.co.fitscan.features.auth.data.usecases

import icesi.edu.co.fitscan.features.auth.data.dto.Customer
import icesi.edu.co.fitscan.features.auth.data.repositories.AuthRepository

class CustomerRegistrationUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(
        userId: String, // Now expecting UUID, not email
        age: Int,
        phoneNumber: String
    ): Result<Unit> {
        try {
            val customer = Customer(user_id = userId, age = age, phone = phoneNumber)
            return authRepository.registerCustomer(customer)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}