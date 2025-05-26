package icesi.edu.co.fitscan.features.workout.data.repositories

import icesi.edu.co.fitscan.domain.model.WorkoutExercise
import icesi.edu.co.fitscan.domain.repositories.IWorkoutExerciseRepository
import android.util.Log
import icesi.edu.co.fitscan.features.workout.data.dataSources.IWorkoutExerciseDataSource
import icesi.edu.co.fitscan.features.workout.data.dto.WorkoutExerciseDto
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
                val workoutExercises =
                    response.body()?.data?.data?.map { dto: WorkoutExerciseDto -> mapper.toDomain(dto) }
                        ?: emptyList()
                Result.success(workoutExercises)
            } else {
                Result.failure(Exception("Error getting workout exercises: \\${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addExerciseToWorkout(workoutExercise: WorkoutExercise): Result<WorkoutExercise> {
        return try {

            val response = datasource.addExerciseToWorkout(mapper.toDto(workoutExercise))

            if (response.isSuccessful) {
                val workoutExercise =
                    response.body()?.let { dto: WorkoutExerciseDto -> mapper.toDomain(dto) }
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

    override suspend fun removeExerciseFromWorkout(
        workoutId: UUID,
        exerciseId: UUID
    ): Result<Boolean> {
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

    override suspend fun updateWorkoutExercise(
        workoutId: UUID,
        exerciseId: UUID,
        workoutExercise: WorkoutExercise
    ): Result<WorkoutExercise> {
        return try {
            val response = datasource.updateWorkoutExercise(
                workoutId.toString(),
                exerciseId.toString(),
                mapper.toDto(workoutExercise)
            )

            if (response.isSuccessful) {
                val updatedExercise =
                    response.body()?.let { dto: WorkoutExerciseDto -> mapper.toDomain(dto) }
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

    override suspend fun markExerciseAsCompleted(
        workoutId: UUID,
        exerciseId: UUID
    ): Result<WorkoutExercise> {
        return try {
            val response = datasource.markExerciseAsCompleted(
                workoutId.toString(),
                exerciseId.toString()
            )

            if (response.isSuccessful) {
                val completedExercise =
                    response.body()?.let { dto: WorkoutExerciseDto -> mapper.toDomain(dto) }
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

    override suspend fun reorderWorkoutExercises(
        workoutId: UUID,
        exerciseOrder: List<String>
    ): Result<List<WorkoutExercise>> {
        return try {
            val response = datasource.reorderWorkoutExercise(
                workoutId.toString(),
                exerciseOrder
            )

            if (response.isSuccessful) {
                val workoutExercises =
                    response.body()?.map { dto: WorkoutExerciseDto -> mapper.toDomain(dto) }
                        ?: emptyList()
                Result.success(workoutExercises)
            } else {
                Result.failure(Exception("Error reordering workout exercises: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getWorkoutExerciseById(workoutId: UUID, id: UUID): Result<WorkoutExercise> {
        return try {
            val result = getWorkoutExercises(workoutId)
            val lista = result.getOrNull()
            Log.d("WorkoutRepo", "Buscando id: $id en la lista: ${lista?.map { it.id }}")
            val exercise = lista?.find { it.id == id }
            if (exercise != null) {
                Result.success(exercise)
            } else {
                Log.e("WorkoutRepo", "No se encontró el id: $id en la lista")
                Result.failure(Exception("No se encontró el ejercicio en la rutina"))
            }
        } catch (e: Exception) {
            Log.e("WorkoutRepo", "Error buscando ejercicio", e)
            Result.failure(e)
        }
    }
}
