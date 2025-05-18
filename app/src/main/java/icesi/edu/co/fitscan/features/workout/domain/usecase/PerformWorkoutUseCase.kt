package icesi.edu.co.fitscan.features.workout.domain.usecase

import icesi.edu.co.fitscan.features.workout.domain.data.remote.response.WorkoutResponse
import icesi.edu.co.fitscan.features.workout.domain.service.PerformWorkoutService

class PerformWorkoutUseCase(private val performWorkoutService: PerformWorkoutService) {
    suspend operator fun invoke(customerId: String): Result<WorkoutResponse> {
        if (customerId.isBlank()) {
            return Result.failure(IllegalArgumentException("Customer ID cannot be null or empty"))
        }
        return performWorkoutService.getWorkout(customerId)
    }
}