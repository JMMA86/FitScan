package icesi.edu.co.fitscan.features.workout.data.dataSources

import icesi.edu.co.fitscan.features.workout.data.dto.ApiMultipleResponseDTO
import icesi.edu.co.fitscan.features.workout.data.dto.CompletedExerciseDTO
import retrofit2.http.Body
import retrofit2.http.POST

interface ICompletedExerciseDataSource {
    @POST("items/completed_exercise")
    suspend fun addCompletedExercises(
        @Body completedExercises: List<CompletedExerciseDTO>
    ): ApiMultipleResponseDTO<CompletedExerciseDTO>
}