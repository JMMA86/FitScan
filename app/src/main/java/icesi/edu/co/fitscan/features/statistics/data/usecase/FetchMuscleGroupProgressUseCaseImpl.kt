package icesi.edu.co.fitscan.features.statistics.data.usecase

import icesi.edu.co.fitscan.domain.model.MuscleGroupProgress
import icesi.edu.co.fitscan.domain.repositories.IMuscleGroupRepository
import icesi.edu.co.fitscan.domain.usecases.IFetchMuscleGroupProgressUseCase

class FetchMuscleGroupProgressUseCaseImpl(
    private val repository: IMuscleGroupRepository
) : IFetchMuscleGroupProgressUseCase {
    override suspend operator fun invoke(customerId: String): Result<List<MuscleGroupProgress>> {
        return repository.getMuscleGroupProgress(customerId)
    }
}
