package icesi.edu.co.fitscan.features.workout.data.repositories

import icesi.edu.co.fitscan.features.workout.domain.model.Exercise
import java.util.UUID

interface ExerciseRepository {
    suspend fun getAllExercises(): Result<List<Exercise>>
    suspend fun getExerciseById(id: UUID): Result<Exercise>
    suspend fun createExercise(exercise: Exercise): Result<Exercise>
    suspend fun updateExercise(id: UUID, exercise: Exercise): Result<Exercise>
    suspend fun deleteExercise(id: UUID): Result<Unit>
}