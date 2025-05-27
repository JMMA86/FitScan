package icesi.edu.co.fitscan.domain.usecases

import icesi.edu.co.fitscan.domain.model.CompletedExercise

interface IManageCompleteExerciseUseCase {
    //TODO: Add the others methods to manage completed exercises
    suspend fun addCompletedExercises(completedExercises: List<CompletedExercise>): List<CompletedExercise>
}