package icesi.edu.co.fitscan.features.workout.domain.service

import icesi.edu.co.fitscan.features.workout.domain.data.remote.response.WorkoutExerciseResponse
import icesi.edu.co.fitscan.features.workout.domain.data.remote.response.WorkoutResponse

interface PerformWorkoutService {
    suspend fun getWorkout(customerId: String): Result<WorkoutResponse>
    suspend fun getWorkoutExercises(workoutId: String): Result<WorkoutExerciseResponse>
}