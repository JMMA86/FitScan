package icesi.edu.co.fitscan.features.workout.data.repositories

import icesi.edu.co.fitscan.features.workout.domain.model.Workout
import icesi.edu.co.fitscan.features.workout.domain.model.WorkoutExercise
import java.util.UUID

interface WorkoutRepository {
    suspend fun getAllWorkouts(): Result<List<Workout>>
    suspend fun getWorkoutById(id: UUID): Result<Workout>
    suspend fun createWorkout(workout: Workout): Result<Workout>
    suspend fun updateWorkout(id: UUID, workout: Workout): Result<Workout>
    suspend fun deleteWorkout(id: UUID): Result<Unit>
    
    suspend fun getWorkoutsByCustomerId(customerId: UUID): Result<List<Workout>>
    
} 