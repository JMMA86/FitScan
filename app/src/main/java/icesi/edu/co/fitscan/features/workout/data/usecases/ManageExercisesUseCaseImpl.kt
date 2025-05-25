package icesi.edu.co.fitscan.features.workout.data.usecases

import icesi.edu.co.fitscan.domain.model.Exercise
import icesi.edu.co.fitscan.domain.repositories.IExerciseRepository
import icesi.edu.co.fitscan.domain.usecases.IManageExercisesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID

class ManageExercisesUseCaseImpl(
    private val exerciseRepository: IExerciseRepository
) : IManageExercisesUseCase {
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

    override suspend fun getExerciseById(id: UUID): Exercise {
        try {
            val response = exerciseRepository.getExerciseById(id)
            response.fold(
                onSuccess = { exercise ->
                    return exercise
                },
                onFailure = { throw it }
            )
        } catch (e: Exception) {
            throw e
        }
    }
}