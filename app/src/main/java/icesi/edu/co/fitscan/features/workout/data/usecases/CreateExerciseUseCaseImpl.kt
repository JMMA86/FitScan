package icesi.edu.co.fitscan.features.workout.data.usecases

import icesi.edu.co.fitscan.domain.model.Exercise
import icesi.edu.co.fitscan.domain.repositories.IExerciseRepository
import icesi.edu.co.fitscan.domain.usecases.ICreateExerciseUseCase

class CreateExerciseUseCaseImpl(
    private val exerciseRepository: IExerciseRepository
): ICreateExerciseUseCase {
    override suspend fun invoke(exercise: Exercise): Result<Exercise> {
        return exerciseRepository.createExercise(exercise)
    }

}