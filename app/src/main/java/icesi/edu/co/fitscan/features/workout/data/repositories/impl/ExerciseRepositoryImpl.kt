package icesi.edu.co.fitscan.features.workout.data.repositories.impl

import icesi.edu.co.fitscan.features.workout.data.dataSources.ExerciseDataSource
import icesi.edu.co.fitscan.features.workout.data.dto.ExerciseDto
import icesi.edu.co.fitscan.features.workout.domain.model.Exercise
import icesi.edu.co.fitscan.features.workout.data.repositories.ExerciseRepository
import icesi.edu.co.fitscan.features.workout.data.mapper.ExerciseMapper
import java.util.UUID

class ExerciseRepositoryImpl(
    private val datasource: ExerciseDataSource,
    private val mapper: ExerciseMapper
) : ExerciseRepository {

    override suspend fun getAllExercises(): Result<List<Exercise>> {
        return try {
            val response = datasource.getAllExercises()
            if (response.isSuccessful) {
                val responseBody = response.body()
                val exerciseDto = responseBody?.data ?: emptyList()
                val exercises = exerciseDto.map { dto -> mapper.toDomain(dto) }
                Result.success(exercises)
            } else {
                Result.failure(Exception("Error getting exercises: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getExerciseById(id: UUID): Result<Exercise> {
        return try {
            val response = datasource.getExerciseById(id.toString())
            if (response.isSuccessful) {
                val exercise = response.body()?.let { dto: ExerciseDto -> mapper.toDomain(dto) }
                if (exercise != null) {
                    Result.success(exercise)
                } else {
                    Result.failure(Exception("Exercise not found"))
                }
            } else {
                Result.failure(Exception("Error getting exercise: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createExercise(exercise: Exercise): Result<Exercise> {
        return try {
            val response = datasource.createExercise(mapper.toDto(exercise))
            if (response.isSuccessful) {
                val exercise = response.body()?.let { dto: ExerciseDto -> mapper.toDomain(dto) }
                if (exercise != null) {
                    Result.success(exercise)
                } else {
                    Result.failure(Exception("Error creating exercise"))
                }
            } else {
                Result.failure(Exception("Error creating exercise: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateExercise(id: UUID, exercise: Exercise): Result<Exercise> {
        return try {
            val response = datasource.updateExercise(id.toString(), mapper.toDto(exercise))
            if (response.isSuccessful) {
                val exercise = response.body()?.let { dto: ExerciseDto -> mapper.toDomain(dto) }
                if (exercise != null) {
                    Result.success(exercise)
                } else {
                    Result.failure(Exception("Error updating exercise"))
                }
            } else {
                Result.failure(Exception("Error updating exercise: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteExercise(id: UUID): Result<Unit> {
        return try {
            val response = datasource.deleteExercise(id.toString())
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error deleting exercise: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
