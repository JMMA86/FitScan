package icesi.edu.co.fitscan.features.workout.domain.usecase

import icesi.edu.co.fitscan.features.workout.domain.model.Exercise
import icesi.edu.co.fitscan.features.workout.data.dataSources.ExerciseService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

open class GetExercisesUseCase(
    private val exerciseService: ExerciseService
) {
    operator fun invoke(): Flow<List<Exercise>> = flow {
        try {
            val response = exerciseService.getAllExercises()
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
