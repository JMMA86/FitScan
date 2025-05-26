package icesi.edu.co.fitscan.features.statistics.data.usecase

import icesi.edu.co.fitscan.domain.model.ExerciseItem
import icesi.edu.co.fitscan.domain.repositories.IExerciseProgressRepository
import icesi.edu.co.fitscan.domain.repositories.IProgressPhotoRepository
import icesi.edu.co.fitscan.domain.usecases.IFetchAllExercisesUseCase

class FetchAllExercisesUseCaseImpl(
    private val repository: IExerciseProgressRepository
) : IFetchAllExercisesUseCase {
    override suspend fun invoke(): Result<List<ExerciseItem>> {
        return repository.fetchAllExercises()
    }
}
