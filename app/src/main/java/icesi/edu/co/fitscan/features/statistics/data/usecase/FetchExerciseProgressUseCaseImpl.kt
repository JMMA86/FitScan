package icesi.edu.co.fitscan.features.statistics.data.usecase

import icesi.edu.co.fitscan.domain.model.ExerciseProgressPoint
import icesi.edu.co.fitscan.domain.repositories.IExerciseProgressRepository
import icesi.edu.co.fitscan.domain.usecases.IFetchExerciseProgressUseCase

class FetchExerciseProgressUseCaseImpl(private val repository: IExerciseProgressRepository): IFetchExerciseProgressUseCase {
    override suspend operator fun invoke(
        exerciseId: String,
        fromDate: String,
        toDate: String
    ): Result<List<ExerciseProgressPoint>> {
        return repository.fetchExerciseProgress(exerciseId, fromDate, toDate)
    }
}
