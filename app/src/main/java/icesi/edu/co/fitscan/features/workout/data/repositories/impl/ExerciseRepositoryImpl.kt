package icesi.edu.co.fitscan.features.workout.data.repositories.impl

import icesi.edu.co.fitscan.features.workout.data.api.ExerciseApiService
import icesi.edu.co.fitscan.features.workout.data.dto.ExerciseDto
import icesi.edu.co.fitscan.features.workout.domain.model.Exercise
import icesi.edu.co.fitscan.features.workout.data.repositories.ExerciseRepository
import icesi.edu.co.fitscan.features.workout.domain.mapper.ExerciseMapper
import java.util.UUID

class ExerciseRepositoryImpl(
    private val api: ExerciseApiService,
    private val mapper: ExerciseMapper
) : ExerciseRepository {

    override suspend fun getAllExercises(): Result<List<Exercise>> {
        return try {
            val response = api.getAllExercises()
            if (response.isSuccessful) {
                val exercises = response.body()?.map { dto: ExerciseDto -> mapper.toDomain(dto) } ?: emptyList()
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
            val response = api.getExerciseById(id.toString())
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
            val response = api.createExercise(mapper.toDto(exercise))
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
            val response = api.updateExercise(id.toString(), mapper.toDto(exercise))
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
            val response = api.deleteExercise(id.toString())
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
