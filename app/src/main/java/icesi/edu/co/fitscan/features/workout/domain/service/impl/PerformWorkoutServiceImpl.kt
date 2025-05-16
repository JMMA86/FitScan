package icesi.edu.co.fitscan.features.workout.domain.service.impl

import icesi.edu.co.fitscan.features.common.ui.viewmodel.AppState
import icesi.edu.co.fitscan.features.workout.domain.data.remote.response.WorkoutExerciseResponse
import icesi.edu.co.fitscan.features.workout.domain.data.remote.response.WorkoutResponse
import icesi.edu.co.fitscan.features.workout.domain.repository.PerformWorkoutRepository
import icesi.edu.co.fitscan.features.workout.domain.service.PerformWorkoutService

class PerformWorkoutServiceImpl(
    private val performWorkoutRepository: PerformWorkoutRepository
) : PerformWorkoutService {

    override suspend fun getWorkout(customerId: String): Result<WorkoutResponse> {
        return try {
            val token = AppState.token ?: throw IllegalArgumentException("Token is null")
            val response =
                performWorkoutRepository.getWorkout(token = token, customerId = customerId)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getWorkoutExercises(workoutId: String): Result<WorkoutExerciseResponse> {
        return try {
            val token = AppState.token ?: throw IllegalArgumentException("Token is null")
            val response =
                performWorkoutRepository.getWorkoutExercises(token = token, workoutId = workoutId)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}