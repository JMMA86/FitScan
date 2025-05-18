package icesi.edu.co.fitscan.features.workout.domain.usecase

import icesi.edu.co.fitscan.features.workout.domain.model.Workout
import icesi.edu.co.fitscan.features.workout.data.dataSources.WorkoutService

open class CreateWorkoutUseCase(
    private val workoutService: WorkoutService
) {
    suspend operator fun invoke(workout: Workout): Result<Workout> {
        if (workout.name.isBlank()) {
            return Result.failure(IllegalArgumentException("El nombre del workout no puede estar vacío"))
        }
        return workoutService.createWorkout(workout)
    }
}