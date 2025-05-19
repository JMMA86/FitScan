package icesi.edu.co.fitscan.features.workout.domain.usecase

import icesi.edu.co.fitscan.features.workout.domain.model.WorkoutExercise
import icesi.edu.co.fitscan.features.workout.data.dataSources.WorkoutService
import java.util.UUID

open class ManageWorkoutExercisesUseCase(
    private val workoutService: WorkoutService
) {
    suspend fun addExercise(workoutExercise: WorkoutExercise): Result<WorkoutExercise> {
        if (workoutExercise.sets <= 0 || workoutExercise.reps <= 0) {
            return Result.failure(IllegalArgumentException("Series y repeticiones deben ser mayores a 0"))
        }
        return workoutService.addExerciseToWorkout(workoutExercise)
    }

    suspend fun removeExercise(workoutId: UUID, exerciseId: UUID): Result<Boolean> {
        workoutService.removeExerciseFromWorkout(workoutId, exerciseId)
        return Result.success(true)
    }

    suspend fun updateExercise(workoutId: UUID, exerciseId: UUID, workoutExercise: WorkoutExercise): Result<WorkoutExercise> {
        if (workoutExercise.sets <= 0 || workoutExercise.reps <= 0) {
            return Result.failure(IllegalArgumentException("Series y repeticiones deben ser mayores a 0"))
        }
        return workoutService.updateWorkoutExercise(workoutId, exerciseId, workoutExercise)
    }
}
