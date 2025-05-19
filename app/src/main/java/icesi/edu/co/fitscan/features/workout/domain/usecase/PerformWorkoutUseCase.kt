package icesi.edu.co.fitscan.features.workout.domain.usecase

import icesi.edu.co.fitscan.features.workout.data.dataSources.WorkoutService
import java.util.UUID

class PerformWorkoutUseCase(private val performWorkoutService: WorkoutService) {
    suspend operator fun invoke(customerId: String): Result<Any> {
        if (customerId.isBlank()) {
            return Result.failure(IllegalArgumentException("Customer ID cannot be null or empty"))
        }

        val customerIdUUid = try {
            UUID.fromString(customerId)
        } catch (e: IllegalArgumentException) {
            return Result.failure(IllegalArgumentException("Invalid Customer ID format"))
        }

        return performWorkoutService.getWorkoutsByCustomerId(customerIdUUid)
    }
}