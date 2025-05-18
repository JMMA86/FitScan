package icesi.edu.co.fitscan.features.workout.data.repositories.impl

import icesi.edu.co.fitscan.features.workout.data.api.WorkoutExerciseApiService
import icesi.edu.co.fitscan.features.workout.data.dto.WorkoutExerciseDto
import icesi.edu.co.fitscan.features.workout.domain.model.WorkoutExercise
import icesi.edu.co.fitscan.features.workout.data.repositories.WorkoutExerciseRepository
import icesi.edu.co.fitscan.features.workout.domain.mapper.WorkoutExerciseMapper
import java.util.UUID

class WorkoutExerciseRepositoryImpl(
    private val api: WorkoutExerciseApiService,
    private val mapper: WorkoutExerciseMapper
) : WorkoutExerciseRepository {

    override suspend fun getWorkoutExercises(workoutId: UUID): Result<List<WorkoutExercise>> {
        return try {
            val response = api.getExercisesByWorkoutId(workoutId.toString())
            if (response.isSuccessful) {
                val workoutExercises = response.body()?.map { dto: WorkoutExerciseDto -> mapper.toDomain(dto) } ?: emptyList<WorkoutExercise>()
                Result.success(workoutExercises)
            } else {
                Result.failure(Exception("Error getting workout exercises: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addExerciseToWorkout(workoutId: UUID, exerciseId: UUID): Result<WorkoutExercise> {
        return try {
            
            val response = api.addExerciseToWorkout(
                workoutId.toString(),
                exerciseId.toString()
            )
            
            if (response.isSuccessful) {
                val workoutExercise = response.body()?.let { dto: WorkoutExerciseDto -> mapper.toDomain(dto) }
                if (workoutExercise != null) {
                    Result.success(workoutExercise)
                } else {
                    Result.failure(Exception("Error adding exercise to workout"))
                }
            } else {
                Result.failure(Exception("Error adding exercise to workout: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeExerciseFromWorkout(workoutId: UUID, exerciseId: UUID): Result<Boolean> {
        return try {
            val response = api.removeExerciseFromWorkout(
                workoutId.toString(),
                exerciseId.toString()
            )
            
            if (response.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(Exception("Error removing exercise from workout: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateWorkoutExercise(workoutId: UUID, exerciseId: UUID, workoutExercise: WorkoutExercise): Result<WorkoutExercise> {
        return try {
            val response = api.updateWorkoutExercise(
                workoutId.toString(),
                exerciseId.toString(),
                mapper.toDto(workoutExercise)
            )
            
            if (response.isSuccessful) {
                val updatedExercise = response.body()?.let { dto: WorkoutExerciseDto -> mapper.toDomain(dto) }
                if (updatedExercise != null) {
                    Result.success(updatedExercise)
                } else {
                    Result.failure(Exception("Error updating workout exercise"))
                }
            } else {
                Result.failure(Exception("Error updating workout exercise: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun markExerciseAsCompleted(workoutId: UUID, exerciseId: UUID): Result<WorkoutExercise> {
        return try {
            val response = api.markExerciseAsCompleted(
                workoutId.toString(),
                exerciseId.toString()
            )
            
            if (response.isSuccessful) {
                val completedExercise = response.body()?.let { dto: WorkoutExerciseDto -> mapper.toDomain(dto) }
                if (completedExercise != null) {
                    Result.success(completedExercise)
                } else {
                    Result.failure(Exception("Error marking exercise as completed"))
                }
            } else {
                Result.failure(Exception("Error marking exercise as completed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun reorderWorkoutExercises(workoutId: UUID, exerciseOrder: List<String>): Result<List<WorkoutExercise>> {
        return try {
            val response = api.reorderWorkoutExercise(
                workoutId.toString(),
                exerciseOrder
            )
            
            if (response.isSuccessful) {
                val workoutExercises = response.body()?.map { dto: WorkoutExerciseDto -> mapper.toDomain(dto) } ?: emptyList<WorkoutExercise>()
                Result.success(workoutExercises)
            } else {
                Result.failure(Exception("Error reordering workout exercises: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
