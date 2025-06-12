package icesi.edu.co.fitscan.features.home.data.repositories

import icesi.edu.co.fitscan.domain.model.DashboardResponse
import icesi.edu.co.fitscan.domain.repositories.IDashboardRepository
import icesi.edu.co.fitscan.features.home.data.dataSources.IDashboardDataSource
import icesi.edu.co.fitscan.features.workout.data.mapper.WorkoutMapper

class DashboardRepositoryImpl(
    private val datasource: IDashboardDataSource
) : IDashboardRepository {

    private val workoutMapper = WorkoutMapper()

    override suspend fun getDashboardData(userId: String): Result<DashboardResponse> {
        return try {
            val response = datasource.getDashboardData(userId = userId)
            if (response.isSuccessful) {
                val workoutsResponse = response.body()!!
                // Mapear de WorkoutsResponse a DashboardResponse
                val workouts = workoutsResponse.data.map { dto -> workoutMapper.toDomain(dto) }
                val dashboardResponse = DashboardResponse(data = workouts)
                Result.success(dashboardResponse)
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 