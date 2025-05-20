package icesi.edu.co.fitscan.domain.usecases

import icesi.edu.co.fitscan.domain.model.Exercise
import kotlinx.coroutines.flow.Flow

interface IGetExercisesUseCase {
    operator fun invoke(): Flow<List<Exercise>>
}