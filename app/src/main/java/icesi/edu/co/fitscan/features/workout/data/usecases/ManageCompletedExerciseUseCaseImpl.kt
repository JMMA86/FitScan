package icesi.edu.co.fitscan.features.workout.data.usecases

import android.util.Log
import icesi.edu.co.fitscan.domain.model.CompletedExercise
import icesi.edu.co.fitscan.domain.usecases.IManageCompleteExerciseUseCase
import icesi.edu.co.fitscan.features.workout.data.repositories.CompletedExerciseRepository

class ManageCompletedExerciseUseCaseImpl(
    private val completedExerciseRepository: CompletedExerciseRepository
) : IManageCompleteExerciseUseCase {
    override suspend fun addCompletedExercises(completedExercises: List<CompletedExercise>): List<CompletedExercise> {
        if (completedExercises.isEmpty()) {
            Log.e("ManageCompletedExerciseUseCase", "No completed exercises to add")
            return emptyList()
        }
        return try {
            completedExerciseRepository.addCompletedExercises(completedExercises)
        } catch (e: Exception) {
            Log.e("ManageCompletedExerciseUseCase", "Error adding completed exercises", e)
            emptyList()
        }
    }
}