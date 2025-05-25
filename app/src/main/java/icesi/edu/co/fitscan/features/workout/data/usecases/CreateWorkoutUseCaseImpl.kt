package icesi.edu.co.fitscan.features.workout.data.usecases

import icesi.edu.co.fitscan.domain.model.Workout
import icesi.edu.co.fitscan.domain.model.WorkoutExercise
import icesi.edu.co.fitscan.domain.repositories.IWorkoutExerciseRepository
import icesi.edu.co.fitscan.domain.repositories.IWorkoutRepository
import icesi.edu.co.fitscan.domain.usecases.ICreateWorkoutUseCase

class CreateWorkoutUseCaseImpl(
    private val workoutRepository: IWorkoutRepository,
    private val workoutExerciseRepository: IWorkoutExerciseRepository
) : ICreateWorkoutUseCase {

    override suspend operator fun invoke(workout: Workout, workoutExercises: List<WorkoutExercise>): Result<Workout> {
        if (workout.name.isBlank()) {
            return Result.failure(IllegalArgumentException("El nombre del workout no puede estar vacío"))
        }

        if (workoutExercises.isEmpty()) {
            return Result.failure(IllegalArgumentException("La rutina debe tener al menos un ejercicio"))
        }

        try {
            workoutRepository.createWorkout(workout)

            for (workoutExercise in workoutExercises) {
                workoutExerciseRepository.addExerciseToWorkout(workoutExercise)
            }

            return Result.success(workout)

        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}