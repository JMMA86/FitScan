package icesi.edu.co.fitscan.features.workout.data.dataSources.impl

import icesi.edu.co.fitscan.features.workout.domain.model.Exercise
import icesi.edu.co.fitscan.features.workout.data.repositories.ExerciseRepository
import icesi.edu.co.fitscan.features.workout.data.dataSources.ExerciseService
import java.util.UUID

class ExerciseServiceImpl(
    private val repository: ExerciseRepository
) : ExerciseService {

    override suspend fun getAllExercises(): Result<List<Exercise>> {
        return repository.getAllExercises()
    }

    override suspend fun getExerciseById(id: UUID): Result<Exercise> {
        if (id.toString().isBlank()) return Result.failure(Exception("El ID del ejercicio no puede estar vacío"))
        return repository.getExerciseById(id)
    }

    override suspend fun createExercise(exercise: Exercise): Result<Exercise> {
        if (!validateExercise(exercise)) return Result.failure(Exception("Datos del ejercicio inválidos"))
        return repository.createExercise(exercise)
    }

    override suspend fun updateExercise(id: UUID, exercise: Exercise): Result<Exercise> {
        if (!validateExercise(exercise)) return Result.failure(Exception("Datos del ejercicio inválidos"))
        return repository.updateExercise(id, exercise)
    }

    override suspend fun deleteExercise(id: UUID): Result<Unit> {
        if (id.toString().isBlank()) return Result.failure(Exception("El ID del ejercicio no puede estar vacío"))
        return repository.deleteExercise(id)
    }

    private fun validateExercise(exercise: Exercise): Boolean {
        return exercise.name.isNotBlank() && exercise.description?.isNotBlank() == true && exercise.muscleGroups?.isNotBlank() == true
    }
}
