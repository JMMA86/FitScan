package icesi.edu.co.fitscan.features.workout.data.repositories

import icesi.edu.co.fitscan.domain.model.Workout
import icesi.edu.co.fitscan.domain.repositories.IWorkoutRepository
import icesi.edu.co.fitscan.features.workout.data.dataSources.IWorkoutDataSource
import icesi.edu.co.fitscan.features.workout.data.dto.WorkoutDto
import icesi.edu.co.fitscan.features.workout.data.mapper.WorkoutMapper
import java.util.UUID

class WorkoutRepositoryImpl(
    private val datasource: IWorkoutDataSource,
    private val mapper: WorkoutMapper
) : IWorkoutRepository {

    override suspend fun getAllWorkouts(): Result<List<Workout>> {
        return try {
            val response = datasource.getAllWorkouts()
            if (response.isSuccessful) {
                val workouts =
                    response.body()?.map { dto: WorkoutDto -> mapper.toDomain(dto) } ?: emptyList()
                Result.success(workouts)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getWorkoutsByCustomerId(customerId: UUID): Result<List<Workout>> {
        return try {
            val response = datasource.getWorkoutsByCustomerId(customerId.toString())
            if (response.isSuccessful) {
                val workouts =
                    response.body()?.data?.map { dto: WorkoutDto -> mapper.toDomain(dto) }
                        ?: emptyList()
                Result.success(workouts)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }    override suspend fun getWorkoutById(id: UUID): Result<Workout> {
        return try {
            android.util.Log.d("WorkoutRepositoryImpl", "[getWorkoutById] Requesting workout with id: $id")
            val response = datasource.getWorkoutById(id.toString())
            android.util.Log.d("WorkoutRepositoryImpl", "[getWorkoutById] Response successful: ${response.isSuccessful}, code: ${response.code()}")
            if (response.isSuccessful) {
                val responseBody = response.body()
                android.util.Log.d("WorkoutRepositoryImpl", "[getWorkoutById] Response body: $responseBody")
                val workout = responseBody?.data?.let { dto: WorkoutDto -> 
                    android.util.Log.d("WorkoutRepositoryImpl", "[getWorkoutById] Mapping DTO: $dto")
                    mapper.toDomain(dto) 
                }
                android.util.Log.d("WorkoutRepositoryImpl", "[getWorkoutById] Mapped workout: $workout")
                if (workout != null) {
                    Result.success(workout)
                } else {
                    Result.failure(Exception("Workout not found"))
                }
            } else {
                android.util.Log.e("WorkoutRepositoryImpl", "[getWorkoutById] Error response: ${response.code()}, ${response.errorBody()?.string()}")
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            android.util.Log.e("WorkoutRepositoryImpl", "[getWorkoutById] Exception: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun createWorkout(workout: Workout): Result<Workout> {
        return try {
            val response = datasource.createWorkout(mapper.toDto(workout))
            if (response.isSuccessful) {
                val createdWorkout =
                    response.body()?.let { dto: WorkoutDto -> mapper.toDomain(dto) }
                if (createdWorkout != null) {
                    Result.success(createdWorkout)
                } else {
                    Result.failure(Exception("Error creating workout"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateWorkout(id: UUID, workout: Workout): Result<Workout> {
        return try {
            val response = datasource.updateWorkout(id.toString(), mapper.toDto(workout))
            if (response.isSuccessful) {
                val updatedWorkout =
                    response.body()?.let { dto: WorkoutDto -> mapper.toDomain(dto) }
                if (updatedWorkout != null) {
                    Result.success(updatedWorkout)
                } else {
                    Result.failure(Exception("Error updating workout"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteWorkout(id: UUID): Result<Unit> {
        return try {
            val response = datasource.deleteWorkout(id.toString())
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
