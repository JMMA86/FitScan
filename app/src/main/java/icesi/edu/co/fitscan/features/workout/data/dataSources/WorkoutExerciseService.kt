package icesi.edu.co.fitscan.features.workout.data.dataSources

import icesi.edu.co.fitscan.features.workout.domain.model.WorkoutExercise
import java.util.UUID

interface WorkoutExerciseService {
    suspend fun getWorkoutExercises(workoutId: UUID): Result<List<WorkoutExercise>>
    suspend fun addExerciseToWorkout(workoutExercise: WorkoutExercise): Result<WorkoutExercise>
    suspend fun removeExerciseFromWorkout(workoutId: UUID, exerciseId: UUID): Result<Unit>
    suspend fun updateWorkoutExercise(workoutId: UUID, exerciseId: UUID, workoutExercise: WorkoutExercise): Result<WorkoutExercise>
    suspend fun markExerciseAsCompleted(workoutId: UUID, exerciseId: UUID): Result<WorkoutExercise>
    suspend fun reorderWorkoutExercises(workoutId: UUID, exerciseOrder: List<String>): Result<List<WorkoutExercise>>
}
