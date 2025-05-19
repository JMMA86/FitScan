package icesi.edu.co.fitscan.features.workout.domain.usecase

import icesi.edu.co.fitscan.features.workout.domain.model.WorkoutExercise
import icesi.edu.co.fitscan.features.workout.data.repositories.WorkoutExerciseRepository
import java.util.UUID

open class ManageWorkoutExercisesUseCase(
    private val workoutExerciseRepository: WorkoutExerciseRepository
) {
    suspend fun addExercise(workoutExercise: WorkoutExercise): Result<WorkoutExercise> {
        if (workoutExercise.sets <= 0 || workoutExercise.reps <= 0) {
            return Result.failure(IllegalArgumentException("Series y repeticiones deben ser mayores a 0"))
        }
        return workoutExerciseRepository.addExerciseToWorkout(workoutExercise)
    }

    suspend fun removeExercise(workoutId: UUID, exerciseId: UUID): Result<Boolean> {
        workoutExerciseRepository.removeExerciseFromWorkout(workoutId, exerciseId)
        return Result.success(true)
    }

    suspend fun updateExercise(workoutId: UUID, exerciseId: UUID, workoutExercise: WorkoutExercise): Result<WorkoutExercise> {
        if (workoutExercise.sets <= 0 || workoutExercise.reps <= 0) {
            return Result.failure(IllegalArgumentException("Series y repeticiones deben ser mayores a 0"))
        }
        return workoutExerciseRepository.updateWorkoutExercise(workoutId, exerciseId, workoutExercise)
    }
}
