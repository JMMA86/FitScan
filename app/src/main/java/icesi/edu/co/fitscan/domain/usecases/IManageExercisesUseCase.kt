package icesi.edu.co.fitscan.domain.usecases

import icesi.edu.co.fitscan.domain.model.Exercise
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface IManageExercisesUseCase {
    operator fun invoke(): Flow<List<Exercise>>
    suspend fun getExerciseById(id: UUID): Exercise
}