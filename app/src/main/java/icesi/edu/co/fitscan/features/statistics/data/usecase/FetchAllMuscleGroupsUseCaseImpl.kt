package icesi.edu.co.fitscan.features.statistics.data.usecase

import icesi.edu.co.fitscan.domain.model.MuscleGroup
import icesi.edu.co.fitscan.domain.repositories.IMuscleGroupRepository
import icesi.edu.co.fitscan.domain.usecases.IFetchAllMuscleGroupsUseCase

class FetchAllMuscleGroupsUseCaseImpl(
    private val repository: IMuscleGroupRepository
) : IFetchAllMuscleGroupsUseCase {
    override suspend operator fun invoke(): Result<List<MuscleGroup>> {
        return repository.getAllMuscleGroups()
    }
}
