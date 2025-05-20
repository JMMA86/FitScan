package icesi.edu.co.fitscan.domain.repositories

import icesi.edu.co.fitscan.domain.model.Exercise
import java.util.UUID

interface IExerciseRepository {
    suspend fun getAllExercises(): Result<List<Exercise>>
    suspend fun getExerciseById(id: UUID): Result<Exercise>
    suspend fun createExercise(exercise: Exercise): Result<Exercise>
    suspend fun updateExercise(id: UUID, exercise: Exercise): Result<Exercise>
    suspend fun deleteExercise(id: UUID): Result<Unit>
}