package icesi.edu.co.fitscan.domain.usecases

import icesi.edu.co.fitscan.domain.model.MuscleGroupProgress

interface IFetchMuscleGroupProgressUseCase {
    suspend operator fun invoke(customerId: String): Result<List<MuscleGroupProgress>>
}
