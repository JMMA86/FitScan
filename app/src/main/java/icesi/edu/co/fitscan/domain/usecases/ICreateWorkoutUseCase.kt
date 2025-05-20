package icesi.edu.co.fitscan.domain.usecases

import icesi.edu.co.fitscan.domain.model.Workout
import icesi.edu.co.fitscan.domain.model.WorkoutExercise

interface ICreateWorkoutUseCase {
    suspend operator fun invoke(workout: Workout, workoutExercises: List<WorkoutExercise>): Result<Workout>
}