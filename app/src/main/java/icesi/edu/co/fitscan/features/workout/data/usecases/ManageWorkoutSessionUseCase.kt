package icesi.edu.co.fitscan.features.workout.data.usecases

import icesi.edu.co.fitscan.domain.model.WorkoutSession
import icesi.edu.co.fitscan.domain.usecases.IManageWorkoutSessionUseCase
import icesi.edu.co.fitscan.features.workout.data.repositories.WorkoutSessionRepositoryImpl

class ManageWorkoutSessionUseCaseImpl(
    private val workoutSessionRepository: WorkoutSessionRepositoryImpl
) : IManageWorkoutSessionUseCase {
    override suspend fun addWorkoutSession(
        workoutId: String,
        session: WorkoutSession
    ): Result<String> {
        return try {
            val response = workoutSessionRepository.createWorkoutSession(session)
            val sessionId = response?.body()?.id
                ?: return Result.failure(Exception("No session ID returned"))
            Result.success(sessionId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}