package icesi.edu.co.fitscan.features.workout.data.repositories.impl

import icesi.edu.co.fitscan.features.workout.data.dataSources.WorkoutDataSource
import icesi.edu.co.fitscan.features.workout.data.dto.WorkoutDto
import icesi.edu.co.fitscan.features.workout.domain.model.Workout
import icesi.edu.co.fitscan.features.workout.data.repositories.WorkoutRepository
import icesi.edu.co.fitscan.features.workout.data.mapper.WorkoutMapper
import java.util.UUID

class WorkoutRepositoryImpl(
    private val datasource: WorkoutDataSource,
    private val mapper: WorkoutMapper
) : WorkoutRepository {

    override suspend fun getAllWorkouts(): Result<List<Workout>> {
        return try {
            val response = datasource.getAllWorkouts()
            if (response.isSuccessful) {
                val workouts = response.body()?.map { dto: WorkoutDto -> mapper.toDomain(dto) } ?: emptyList()
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
                val workouts = response.body()?.map { dto: WorkoutDto -> mapper.toDomain(dto) } ?: emptyList()
                Result.success(workouts)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getWorkoutById(id: UUID): Result<Workout> {
        return try {
            val response = datasource.getWorkoutById(id.toString())
            if (response.isSuccessful) {
                val workout = response.body()?.let { dto: WorkoutDto -> mapper.toDomain(dto) }
                if (workout != null) {
                    Result.success(workout)
                } else {
                    Result.failure(Exception("Workout not found"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createWorkout(workout: Workout): Result<Workout> {
        return try {
            val response = datasource.createWorkout(mapper.toDto(workout))
            if (response.isSuccessful) {
                val createdWorkout = response.body()?.let { dto: WorkoutDto -> mapper.toDomain(dto) }
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
                val updatedWorkout = response.body()?.let { dto: WorkoutDto -> mapper.toDomain(dto) }
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
