package icesi.edu.co.fitscan.domain.usecases

import icesi.edu.co.fitscan.domain.model.WorkoutExercise
import java.util.UUID

interface IManageWorkoutExercisesUseCase {
    suspend fun addExercise(workoutExercise: WorkoutExercise): Result<WorkoutExercise>
    suspend fun removeExercise(workoutId: UUID, exerciseId: UUID): Result<Boolean>
    suspend fun updateExercise(
        workoutId: UUID,
        exerciseId: UUID,
        workoutExercise: WorkoutExercise
    ): Result<WorkoutExercise>

    suspend fun getWorkoutExercises(workoutId: UUID): Result<List<WorkoutExercise>>
}