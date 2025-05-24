package icesi.edu.co.fitscan.features.workout.data.usecases

import icesi.edu.co.fitscan.domain.model.Exercise
import icesi.edu.co.fitscan.domain.repositories.IExerciseRepository
import icesi.edu.co.fitscan.domain.usecases.IGetExercisesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetExercisesUseCaseImpl(
    private val exerciseRepository: IExerciseRepository
) : IGetExercisesUseCase {

    override operator fun invoke(): Flow<List<Exercise>> = flow {
        try {
            val response = exerciseRepository.getAllExercises()
            response.fold(
                onSuccess = { exercises ->
                    emit(exercises)
                },
                onFailure = { throw it }
            )
        } catch (e: Exception) {
            throw e
        }
    }
}