package icesi.edu.co.fitscan.domain.usecases

import icesi.edu.co.fitscan.domain.model.Workout
import java.util.UUID

interface IGetWorkoutsUseCase {
    suspend operator fun invoke(customerId: UUID? = null): Result<List<Workout>>
}