package icesi.edu.co.fitscan.features.workout.data.repositories.impl

import icesi.edu.co.fitscan.features.workout.data.api.WorkoutApiService
import icesi.edu.co.fitscan.features.workout.data.api.WorkoutExerciseApiService
import icesi.edu.co.fitscan.features.workout.data.dto.WorkoutExerciseDto
import icesi.edu.co.fitscan.features.workout.data.dto.WorkoutDto
import icesi.edu.co.fitscan.features.workout.domain.mapper.WorkoutExerciseMapper
import icesi.edu.co.fitscan.features.workout.domain.model.Workout
import icesi.edu.co.fitscan.features.workout.domain.model.WorkoutExercise
import icesi.edu.co.fitscan.features.workout.data.repositories.WorkoutRepository
import icesi.edu.co.fitscan.features.workout.domain.mapper.WorkoutMapper
import java.util.UUID

class WorkoutRepositoryImpl(
    private val workoutApiService: WorkoutApiService,
    private val workoutExerciseApiService: WorkoutExerciseApiService,
    private val workoutMapper: WorkoutMapper,
    private val workoutExerciseMapper: WorkoutExerciseMapper
) : WorkoutRepository {

    override suspend fun getAllWorkouts(): Result<List<Workout>> {
        return try {
            val response = workoutApiService.getAllWorkouts()
            if (response.isSuccessful) {
                val workouts = response.body()?.map { dto: WorkoutDto -> workoutMapper.toDomain(dto) } ?: emptyList<Workout>()
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
            val response = workoutApiService.getWorkoutsByCustomerId(customerId.toString())
            if (response.isSuccessful) {
                val workouts = response.body()?.map { dto: WorkoutDto -> workoutMapper.toDomain(dto) } ?: emptyList<Workout>()
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
            val response = workoutApiService.getWorkoutById(id.toString())
            if (response.isSuccessful) {
                val workout = response.body()?.let { dto: WorkoutDto -> workoutMapper.toDomain(dto) }
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
            val response = workoutApiService.createWorkout(workoutMapper.toDto(workout))
            if (response.isSuccessful) {
                val createdWorkout = response.body()?.let { dto: WorkoutDto -> workoutMapper.toDomain(dto) }
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
            val response = workoutApiService.updateWorkout(id.toString(), workoutMapper.toDto(workout))
            if (response.isSuccessful) {
                val updatedWorkout = response.body()?.let { dto: WorkoutDto -> workoutMapper.toDomain(dto) }
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
            val response = workoutApiService.deleteWorkout(id.toString())
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addExerciseToWorkout(workoutId: UUID, exerciseId: UUID): Result<WorkoutExercise> {
        return try {
            val response = workoutExerciseApiService.addExerciseToWorkout(
                workoutId.toString(),
                exerciseId.toString()
            )
            if (response.isSuccessful) {
                val addedExercise = response.body()?.let { dto: WorkoutExerciseDto -> workoutExerciseMapper.toDomain(dto) }
                if (addedExercise != null) {
                    Result.success(addedExercise)
                } else {
                    Result.failure(Exception("Error adding exercise to workout"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeExerciseFromWorkout(workoutId: UUID, exerciseId: UUID): Result<Unit> {
        return try {
            val response = workoutApiService.removeExerciseFromWorkout(workoutId.toString(), exerciseId.toString())
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateWorkoutExercise(workoutId: UUID, exerciseId: UUID, workoutExercise: WorkoutExercise): Result<WorkoutExercise> {
        return try {
            val response = workoutExerciseApiService.updateWorkoutExercise(
                workoutId.toString(),
                exerciseId.toString(),
                workoutExerciseMapper.toDto(workoutExercise)
            )
            if (response.isSuccessful) {
                val updatedExercise = response.body()?.let { dto: WorkoutExerciseDto -> workoutExerciseMapper.toDomain(dto) }
                if (updatedExercise != null) {
                    Result.success(updatedExercise)
                } else {
                    Result.failure(Exception("Error updating workout exercise"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
