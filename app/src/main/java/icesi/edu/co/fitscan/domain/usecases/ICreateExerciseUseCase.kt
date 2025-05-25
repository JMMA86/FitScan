package icesi.edu.co.fitscan.domain.usecases

import icesi.edu.co.fitscan.domain.model.Exercise

interface ICreateExerciseUseCase {
    suspend operator fun invoke(exercise: Exercise): Result<Exercise>
}