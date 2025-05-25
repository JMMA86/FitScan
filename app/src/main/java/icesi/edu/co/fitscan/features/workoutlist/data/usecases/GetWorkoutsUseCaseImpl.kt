package icesi.edu.co.fitscan.features.workoutlist.data.usecases

import icesi.edu.co.fitscan.domain.model.Workout
import icesi.edu.co.fitscan.domain.repositories.IWorkoutRepository
import icesi.edu.co.fitscan.domain.usecases.IGetWorkoutsUseCase
import java.util.UUID

class GetWorkoutsUseCaseImpl(
    private val repository: IWorkoutRepository
) : IGetWorkoutsUseCase {
    override suspend fun invoke(customerId: UUID?): Result<List<Workout>> {
        return if (customerId != null) {
            repository.getWorkoutsByCustomerId(customerId)
        } else {
            repository.getAllWorkouts()
        }
    }
}