package icesi.edu.co.fitscan.domain.usecases

import icesi.edu.co.fitscan.domain.model.WorkoutSession

interface IManageWorkoutSessionUseCase {
    suspend fun addWorkoutSession(
        workoutId: String,
        session: WorkoutSession,
    ): Result<String>
}