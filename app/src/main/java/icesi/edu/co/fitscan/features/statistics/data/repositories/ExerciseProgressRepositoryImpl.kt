package icesi.edu.co.fitscan.features.statistics.data.repositories

import icesi.edu.co.fitscan.domain.model.ExerciseItem
import icesi.edu.co.fitscan.domain.model.ExerciseProgressPoint
import icesi.edu.co.fitscan.domain.repositories.IExerciseProgressRepository
import icesi.edu.co.fitscan.features.statistics.data.mapper.ExerciseProgressMapper
import icesi.edu.co.fitscan.features.statistics.data.remote.StatisticsRemoteDataSource
import icesi.edu.co.fitscan.features.common.ui.viewmodel.AppState
import icesi.edu.co.fitscan.features.statistics.data.mapper.ExerciseItemMapper

class ExerciseProgressRepositoryImpl(
    private val remoteDataSource: StatisticsRemoteDataSource
) : IExerciseProgressRepository {

    override suspend fun fetchExerciseProgress(
        exerciseId: String,
        fromDate: String?,
        toDate: String?
    ): Result<List<ExerciseProgressPoint>> {
        return try {
            val actualCustomerId = AppState.customerId ?: ""
            val points = remoteDataSource.getCompletedExercisesForProgress(
                customerId = actualCustomerId,
                exerciseId = exerciseId,
                fromDate = fromDate,
                toDate = toDate
            ).data
            Result.success(
                ExerciseProgressMapper.toDomainList(points, fromDate, toDate)
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun fetchAllExercises(): Result<List<ExerciseItem>> {
        return try {
            val response = remoteDataSource.getAllExercises()
            val items = response.data.map { ExerciseItemMapper.toDomain(it) }
            Result.success(items)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
