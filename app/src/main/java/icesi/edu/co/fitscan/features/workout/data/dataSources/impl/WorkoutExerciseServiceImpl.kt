package icesi.edu.co.fitscan.features.workout.data.dataSources.impl

import icesi.edu.co.fitscan.features.workout.data.dataSources.WorkoutExerciseService
import icesi.edu.co.fitscan.features.workout.domain.model.WorkoutExercise
import icesi.edu.co.fitscan.features.workout.data.repositories.WorkoutExerciseRepository
import java.util.UUID

class WorkoutExerciseServiceImpl(
    private val repository: WorkoutExerciseRepository
) : WorkoutExerciseService {

    override suspend fun getWorkoutExercises(workoutId: UUID): Result<List<WorkoutExercise>> {
        if (workoutId.toString().isBlank()) return Result.failure(Exception("El ID del entrenamiento no puede estar vacío"))
        return repository.getWorkoutExercises(workoutId)
    }

    override suspend fun addExerciseToWorkout(workoutId: UUID, exerciseId: UUID): Result<WorkoutExercise> {
        if (workoutId.toString().isBlank()) return Result.failure(Exception("El ID del entrenamiento no puede estar vacío"))
        if (exerciseId.toString().isBlank()) return Result.failure(Exception("El ID del ejercicio no puede estar vacío"))
        return repository.addExerciseToWorkout(workoutId, exerciseId)
    }

    override suspend fun removeExerciseFromWorkout(workoutId: UUID, exerciseId: UUID): Result<Unit> {
        if (workoutId.toString().isBlank()) return Result.failure(Exception("El ID del entrenamiento no puede estar vacío"))
        if (exerciseId.toString().isBlank()) return Result.failure(Exception("El ID del ejercicio no puede estar vacío"))
        repository.removeExerciseFromWorkout(workoutId, exerciseId)
        return Result.success(Unit)
    }

    override suspend fun updateWorkoutExercise(workoutId: UUID, exerciseId: UUID, workoutExercise: WorkoutExercise): Result<WorkoutExercise> {
        if (workoutId.toString().isBlank()) return Result.failure(Exception("El ID del entrenamiento no puede estar vacío"))
        if (exerciseId.toString().isBlank()) return Result.failure(Exception("El ID del ejercicio no puede estar vacío"))
        if (!validateWorkoutExercise(workoutExercise)) return Result.failure(Exception("Datos del ejercicio en el entrenamiento inválidos"))
        return repository.updateWorkoutExercise(workoutId, exerciseId, workoutExercise)
    }

    override suspend fun markExerciseAsCompleted(workoutId: UUID, exerciseId: UUID): Result<WorkoutExercise> {
        if (workoutId.toString().isBlank()) return Result.failure(Exception("El ID del entrenamiento no puede estar vacío"))
        if (exerciseId.toString().isBlank()) return Result.failure(Exception("El ID del ejercicio no puede estar vacío"))
        return repository.markExerciseAsCompleted(workoutId, exerciseId)
    }

    override suspend fun reorderWorkoutExercises(workoutId: UUID, exerciseOrder: List<String>): Result<List<WorkoutExercise>> {
        if (workoutId.toString().isBlank()) return Result.failure(Exception("El ID del entrenamiento no puede estar vacío"))
        if (exerciseOrder.isEmpty()) return Result.failure(Exception("La lista de orden de ejercicios no puede estar vacía"))
        return repository.reorderWorkoutExercises(workoutId, exerciseOrder)
    }

    private fun validateWorkoutExercise(workoutExercise: WorkoutExercise): Boolean {
        return workoutExercise.workoutId.toString().isNotBlank() &&
               workoutExercise.exerciseId.toString().isNotBlank() &&
               workoutExercise.sets > 0 &&
               workoutExercise.reps > 0
    }
}
