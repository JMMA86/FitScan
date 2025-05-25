package icesi.edu.co.fitscan.domain.usecases

import icesi.edu.co.fitscan.domain.model.Workout
import icesi.edu.co.fitscan.domain.model.WorkoutExercise
import java.util.UUID

interface IManageWorkoutUseCase {
    suspend operator fun invoke(
        workout: Workout,
        workoutExercises: List<WorkoutExercise>
    ): Result<Workout>

    suspend fun getWorkoutById(id: UUID): Result<Workout>
}