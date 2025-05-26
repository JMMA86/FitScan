package icesi.edu.co.fitscan.domain.usecases

import icesi.edu.co.fitscan.domain.model.ExerciseItem

interface IFetchAllExercisesUseCase {
    suspend operator fun invoke(): Result<List<ExerciseItem>>
}
