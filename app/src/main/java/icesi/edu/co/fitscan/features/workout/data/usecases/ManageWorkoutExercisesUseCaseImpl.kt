package icesi.edu.co.fitscan.features.workout.data.usecases

import icesi.edu.co.fitscan.domain.model.WorkoutExercise
import icesi.edu.co.fitscan.domain.repositories.IWorkoutExerciseRepository
import icesi.edu.co.fitscan.domain.usecases.IManageWorkoutExercisesUseCase
import java.util.UUID

class ManageWorkoutExercisesUseCaseImpl(
    private val workoutExerciseRepository: IWorkoutExerciseRepository,
) : IManageWorkoutExercisesUseCase {

    override suspend fun addExercise(workoutExercise: WorkoutExercise): Result<WorkoutExercise> {
        if (workoutExercise.sets <= 0 || workoutExercise.reps <= 0) {
            return Result.failure(IllegalArgumentException("Series y repeticiones deben ser mayores a 0"))
        }
        return workoutExerciseRepository.addExerciseToWorkout(workoutExercise)
    }

    override suspend fun removeExercise(workoutId: UUID, exerciseId: UUID): Result<Boolean> {
        workoutExerciseRepository.removeExerciseFromWorkout(workoutId, exerciseId)
        return Result.success(true)
    }

    override suspend fun updateExercise(
        workoutId: UUID,
        exerciseId: UUID,
        workoutExercise: WorkoutExercise
    ): Result<WorkoutExercise> {
        if (workoutExercise.sets <= 0 || workoutExercise.reps <= 0) {
            return Result.failure(IllegalArgumentException("Series y repeticiones deben ser mayores a 0"))
        }
        return workoutExerciseRepository.updateWorkoutExercise(
            workoutId,
            exerciseId,
            workoutExercise
        )
    }

    override suspend fun getWorkoutExercises(workoutId: UUID): Result<List<WorkoutExercise>> {
        return workoutExerciseRepository.getWorkoutExercises(workoutId)
    }
}