package icesi.edu.co.fitscan.features.workout.data.dataSources.impl

import icesi.edu.co.fitscan.features.workout.data.dataSources.WorkoutService
import icesi.edu.co.fitscan.features.workout.data.repositories.WorkoutExerciseRepository
import icesi.edu.co.fitscan.features.workout.domain.model.Workout
import icesi.edu.co.fitscan.features.workout.domain.model.WorkoutExercise
import icesi.edu.co.fitscan.features.workout.data.repositories.WorkoutRepository
import java.util.*

class WorkoutServiceImpl(
    private val workoutRepository: WorkoutRepository,
    private val workoutExerciseRepository: WorkoutExerciseRepository
) : WorkoutService {

    override suspend fun getWorkoutsByCustomerId(customerId: UUID): Result<List<Workout>> {
        if (customerId.toString().isBlank()) return Result.failure(Exception("El ID del cliente no puede estar vacío"))
        return workoutRepository.getWorkoutsByCustomerId(customerId)
    }

    override suspend fun getWorkoutById(id: UUID): Result<Workout> {
        if (id.toString().isBlank()) return Result.failure(Exception("El ID del entrenamiento no puede estar vacío"))
        return workoutRepository.getWorkoutById(id)
    }

    override suspend fun createWorkout(workout: Workout): Result<Workout> {
        if (!validateWorkout(workout)) return Result.failure(Exception("Datos del entrenamiento inválidos"))
        return workoutRepository.createWorkout(workout)
    }

    override suspend fun updateWorkout(id: UUID, workout: Workout): Result<Workout> {
        if (!validateWorkout(workout)) return Result.failure(Exception("Datos del entrenamiento inválidos"))
        return workoutRepository.updateWorkout(id, workout)
    }

    override suspend fun deleteWorkout(id: UUID): Result<Unit> {
        if (id.toString().isBlank()) return Result.failure(Exception("El ID del entrenamiento no puede estar vacío"))
        return workoutRepository.deleteWorkout(id)
    }

    override suspend fun addExerciseToWorkout(workoutExercise: WorkoutExercise): Result<WorkoutExercise> {
        if (!validateWorkoutExercise(workoutExercise)) return Result.failure(Exception("Datos del ejercicio en el entrenamiento inválidos"))
        return workoutExerciseRepository.addExerciseToWorkout(workoutExercise)
    }

    override suspend fun removeExerciseFromWorkout(workoutId: UUID, exerciseId: UUID): Result<Unit> {
        if (workoutId.toString().isBlank()) return Result.failure(Exception("El ID del entrenamiento no puede estar vacío"))
        if (exerciseId.toString().isBlank()) return Result.failure(Exception("El ID del ejercicio no puede estar vacío"))
        workoutExerciseRepository.removeExerciseFromWorkout(workoutId, exerciseId)
        return Result.success(Unit)
    }

    override suspend fun updateWorkoutExercise(workoutId: UUID, exerciseId: UUID, workoutExercise: WorkoutExercise): Result<WorkoutExercise> {
        if (!validateWorkoutExercise(workoutExercise)) return Result.failure(Exception("Datos del ejercicio en el entrenamiento inválidos"))
        return workoutExerciseRepository.updateWorkoutExercise(workoutId, exerciseId, workoutExercise)
    }

    private fun validateWorkout(workout: Workout): Boolean {
        return workout.name.isNotBlank() && workout.customerId.toString().isNotBlank() && workout.dateCreated.toString().isNotBlank() && workout.durationMinutes.toString().isNotBlank() && workout.difficulty.toString().isNotBlank()
    }

    private fun validateWorkoutExercise(workoutExercise: WorkoutExercise): Boolean {
        return workoutExercise.workoutId.toString().isNotBlank() && workoutExercise.exerciseId.toString().isNotBlank() && workoutExercise.sets > 0 && workoutExercise.reps > 0
    }
} 