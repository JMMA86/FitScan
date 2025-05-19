package icesi.edu.co.fitscan.features.workout.domain.usecase

import icesi.edu.co.fitscan.features.workout.data.dataSources.WorkoutExerciseService
import icesi.edu.co.fitscan.features.workout.domain.model.Workout
import icesi.edu.co.fitscan.features.workout.domain.model.WorkoutExercise
import icesi.edu.co.fitscan.features.workout.data.dataSources.WorkoutService

class CreateWorkoutUseCase(
    private val workoutService: WorkoutService,
    private val workoutExerciseService: WorkoutExerciseService
) {
    suspend operator fun invoke(workout: Workout, workoutExercises: List<WorkoutExercise>): Result<Workout> {
        // Validaciones básicas
        if (workout.name.isBlank()) {
            return Result.failure(IllegalArgumentException("El nombre del workout no puede estar vacío"))
        }
        
        if (workoutExercises.isEmpty()) {
            return Result.failure(IllegalArgumentException("La rutina debe tener al menos un ejercicio"))
        }
        
        try {
            val workoutResult = workoutService.createWorkout(workout)

            if (workoutResult.isFailure) {
                return workoutResult
            }

            for (workoutExercise in workoutExercises) {
                val exerciseResult = workoutExerciseService.addExerciseToWorkout(workoutExercise)

                if (exerciseResult.isFailure) {
                    return Result.failure(exerciseResult.exceptionOrNull()
                        ?: IllegalStateException("Error al agregar ejercicio"))
                }
            }
            
            return Result.success(workoutResult.getOrThrow())
            
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}