package icesi.edu.co.fitscan.domain.usecases

import icesi.edu.co.fitscan.domain.model.ExerciseProgressPoint

interface IFetchExerciseProgressUseCase {
    suspend operator fun invoke(
        exerciseId: String,
        fromDate: String,
        toDate: String
    ): Result<List<ExerciseProgressPoint>>
}
