package icesi.edu.co.fitscan.features.workout.data.repositories

import icesi.edu.co.fitscan.features.workout.domain.model.WorkoutExercise
import java.util.UUID

interface WorkoutExerciseRepository {
    suspend fun getWorkoutExercises(workoutId: UUID): Result<List<WorkoutExercise>>
    suspend fun addExerciseToWorkout(workoutId: UUID, exerciseId: UUID): Result<WorkoutExercise>
    suspend fun removeExerciseFromWorkout(workoutId: UUID, exerciseId: UUID): Result<Boolean>
    suspend fun updateWorkoutExercise(workoutId: UUID, exerciseId: UUID, workoutExercise: WorkoutExercise): Result<WorkoutExercise>
    suspend fun markExerciseAsCompleted(workoutId: UUID, exerciseId: UUID): Result<WorkoutExercise>
    suspend fun reorderWorkoutExercises(workoutId: UUID, exerciseOrder: List<String>): Result<List<WorkoutExercise>>
}

