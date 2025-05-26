package icesi.edu.co.fitscan.domain.repositories

import icesi.edu.co.fitscan.domain.model.ExerciseItem
import icesi.edu.co.fitscan.domain.model.ExerciseProgressPoint

interface IExerciseProgressRepository {
    suspend fun fetchExerciseProgress(
        exerciseId: String,
        fromDate: String?,
        toDate: String?
    ): Result<List<ExerciseProgressPoint>>

    suspend fun fetchAllExercises(): Result<List<ExerciseItem>>
}
