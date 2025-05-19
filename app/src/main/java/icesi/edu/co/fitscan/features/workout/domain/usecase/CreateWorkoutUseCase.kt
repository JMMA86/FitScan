package icesi.edu.co.fitscan.features.workout.domain.usecase

import icesi.edu.co.fitscan.features.workout.data.repositories.WorkoutRepository
import icesi.edu.co.fitscan.features.workout.data.repositories.WorkoutExerciseRepository
import icesi.edu.co.fitscan.features.workout.domain.model.Workout
import icesi.edu.co.fitscan.features.workout.domain.model.WorkoutExercise

class CreateWorkoutUseCase(
    private val workoutRepository: WorkoutRepository,
    private val workoutExerciseRepository: WorkoutExerciseRepository
) {
    suspend operator fun invoke(workout: Workout, workoutExercises: List<WorkoutExercise>): Result<Workout> {
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