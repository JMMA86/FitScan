package icesi.edu.co.fitscan.features.workout.data.repositories

import icesi.edu.co.fitscan.features.workout.data.dataSources.IExerciseDataSource
import icesi.edu.co.fitscan.features.workout.data.dto.ExerciseDto
import icesi.edu.co.fitscan.domain.model.Exercise
import icesi.edu.co.fitscan.domain.repositories.IExerciseRepository
import icesi.edu.co.fitscan.features.workout.data.mapper.ExerciseMapper
import java.util.UUID

class ExerciseRepositoryImpl(
    private val datasource: IExerciseDataSource,
    private val mapper: ExerciseMapper
) : IExerciseRepository {    override suspend fun getAllExercises(): Result<List<Exercise>> {
        return try {
            android.util.Log.d("ExerciseRepositoryImpl", "[getAllExercises] Requesting all exercises")
            val response = datasource.getAllExercises()
            android.util.Log.d("ExerciseRepositoryImpl", "[getAllExercises] Response successful: ${response.isSuccessful}, code: ${response.code()}")
            if (response.isSuccessful) {
                val responseBody = response.body()
                android.util.Log.d("ExerciseRepositoryImpl", "[getAllExercises] Response body: $responseBody")
                val exerciseDto = responseBody?.data ?: emptyList()
                android.util.Log.d("ExerciseRepositoryImpl", "[getAllExercises] DTOs: $exerciseDto")
                val exercises = exerciseDto.map { dto ->
                    android.util.Log.d("ExerciseRepositoryImpl", "[getAllExercises] Mapping dto: $dto")
                    mapper.toDomain(dto)
                }
                android.util.Log.d("ExerciseRepositoryImpl", "[getAllExercises] Exercises: $exercises")
                Result.success(exercises)
            } else {
                android.util.Log.e("ExerciseRepositoryImpl", "[getAllExercises] Error response: ${response.code()}, ${response.errorBody()?.string()}")
                Result.failure(Exception("Error getting exercises: ${response.code()}"))
            }
        } catch (e: Exception) {
            android.util.Log.e("ExerciseRepositoryImpl", "[getAllExercises] Exception: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun getExerciseById(id: UUID): Result<Exercise> {
        return try {
            val response = datasource.getExerciseById(id.toString())
            if (response.isSuccessful) {
                val responseBody = response.body()
                val dto = responseBody?.data
                android.util.Log.d("ExerciseRepositoryImpl", "[getExerciseById] DTO: $dto")
                val exercise = dto?.let { d ->
                    mapper.toDomain(d)
                }
                android.util.Log.d("ExerciseRepositoryImpl", "[getExerciseById] Exercise: $exercise")
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
