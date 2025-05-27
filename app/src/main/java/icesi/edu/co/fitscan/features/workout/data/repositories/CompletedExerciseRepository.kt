package icesi.edu.co.fitscan.features.workout.data.repositories

import icesi.edu.co.fitscan.domain.model.CompletedExercise
import icesi.edu.co.fitscan.features.workout.data.dataSources.ICompletedExerciseDataSource
import icesi.edu.co.fitscan.features.workout.data.mapper.CompletedExerciseMapper

class CompletedExerciseRepository(
    private val dataSource: ICompletedExerciseDataSource,
    private val mapper: CompletedExerciseMapper
) {
    suspend fun addCompletedExercises(
        completedExercises: List<CompletedExercise>
    ): List<CompletedExercise> {
        val completedExerciseDTOs = completedExercises.map { mapper.toDto(it) }
        val response = dataSource.addCompletedExercises(
            //ApiMultipleResponseDTO(
            completedExerciseDTOs
            //)
        )
        return response.data.map { mapper.fromDto(it) }
    }
}