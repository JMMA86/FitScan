package icesi.edu.co.fitscan.features.workout.data.dataSources

import icesi.edu.co.fitscan.features.workout.domain.model.Workout
import icesi.edu.co.fitscan.features.workout.domain.model.WorkoutExercise
import java.util.UUID

interface WorkoutService {
    suspend fun getWorkoutsByCustomerId(customerId: UUID): Result<List<Workout>>
    suspend fun getWorkoutById(id: UUID): Result<Workout>
    suspend fun createWorkout(workout: Workout): Result<Workout>
    suspend fun updateWorkout(id: UUID, workout: Workout): Result<Workout>
    suspend fun deleteWorkout(id: UUID): Result<Unit>
    
    suspend fun addExerciseToWorkout(workoutExercise: WorkoutExercise): Result<WorkoutExercise>
    suspend fun removeExerciseFromWorkout(workoutId: UUID, exerciseId: UUID): Result<Unit>
    suspend fun updateWorkoutExercise(workoutId: UUID, exerciseId: UUID, workoutExercise: WorkoutExercise): Result<WorkoutExercise>
} 