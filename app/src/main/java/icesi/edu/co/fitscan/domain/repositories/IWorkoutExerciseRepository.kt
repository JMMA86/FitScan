package icesi.edu.co.fitscan.domain.repositories

import icesi.edu.co.fitscan.domain.model.WorkoutExercise
import java.util.UUID

interface IWorkoutExerciseRepository {
    suspend fun getWorkoutExercises(workoutId: UUID): Result<List<WorkoutExercise>>
    suspend fun addExerciseToWorkout(workoutExercise: WorkoutExercise): Result<WorkoutExercise>
    suspend fun removeExerciseFromWorkout(workoutId: UUID, exerciseId: UUID): Result<Boolean>
    suspend fun updateWorkoutExercise(workoutId: UUID, exerciseId: UUID, workoutExercise: WorkoutExercise): Result<WorkoutExercise>
    suspend fun markExerciseAsCompleted(workoutId: UUID, exerciseId: UUID): Result<WorkoutExercise>
    suspend fun reorderWorkoutExercises(workoutId: UUID, exerciseOrder: List<String>): Result<List<WorkoutExercise>>
}

