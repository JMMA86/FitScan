package icesi.edu.co.fitscan.domain.usecases

import icesi.edu.co.fitscan.domain.model.MuscleGroup

interface IFetchAllMuscleGroupsUseCase {
    suspend operator fun invoke(): Result<List<MuscleGroup>>
}
