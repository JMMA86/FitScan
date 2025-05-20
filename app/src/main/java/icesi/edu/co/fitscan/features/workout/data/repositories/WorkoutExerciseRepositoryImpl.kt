package icesi.edu.co.fitscan.features.workout.data.repositories

import icesi.edu.co.fitscan.features.workout.data.dataSources.IWorkoutExerciseDataSource
import icesi.edu.co.fitscan.features.workout.data.dto.WorkoutExerciseDto
import icesi.edu.co.fitscan.domain.model.WorkoutExercise
import icesi.edu.co.fitscan.domain.repositories.IWorkoutExerciseRepository
import icesi.edu.co.fitscan.features.workout.data.mapper.WorkoutExerciseMapper
import java.util.UUID

class WorkoutExerciseRepositoryImpl(
    private val datasource: IWorkoutExerciseDataSource,
    private val mapper: WorkoutExerciseMapper
) : IWorkoutExerciseRepository {

    override suspend fun getWorkoutExercises(workoutId: UUID): Result<List<WorkoutExercise>> {
        return try {
            val response = datasource.getExercisesByWorkoutId(workoutId.toString())
            if (response.isSuccessful) {
                val workoutExercises = response.body()?.map { dto: WorkoutExerciseDto -> mapper.toDomain(dto) } ?: emptyList()
                Result.success(workoutExercises)
            } else {
                Result.failure(Exception("Error getting workout exercises: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addExerciseToWorkout(workoutExercise: WorkoutExercise): Result<WorkoutExercise> {
        return try {
            
            val response = datasource.addExerciseToWorkout(mapper.toDto(workoutExercise))
            
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
            val response = datasource.removeExerciseFromWorkout(
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
            val response = datasource.updateWorkoutExercise(
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
            val response = datasource.markExerciseAsCompleted(
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
            val response = datasource.reorderWorkoutExercise(
                workoutId.toString(),
                exerciseOrder
            )
            
            if (response.isSuccessful) {
                val workoutExercises = response.body()?.map { dto: WorkoutExerciseDto -> mapper.toDomain(dto) } ?: emptyList()
                Result.success(workoutExercises)
            } else {
                Result.failure(Exception("Error reordering workout exercises: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
