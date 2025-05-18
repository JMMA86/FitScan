package icesi.edu.co.fitscan.features.workout.data.repository

import icesi.edu.co.fitscan.features.common.data.remote.RetrofitInstance
import icesi.edu.co.fitscan.features.workout.data.model.Exercise
import retrofit2.Response

class ExerciseRepository {
    // Usa el mismo token JWT que en WorkoutRepository
    private val jwtToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6ImZmNzAzNzZjLWMzODktNGIzNS05NGM2LTkzOTBkZWUxZGU4OCIsInJvbGUiOiIzYWFmYTc5My1kNmZmLTRiNDAtYjYyMC04YWNmYjM1NjgwNWUiLCJhcHBfYWNjZXNzIjp0cnVlLCJhZG1pbl9hY2Nlc3MiOnRydWUsImlhdCI6MTc0NzU4NjU4MCwiZXhwIjoxNzQ3NTkwMTgwLCJpc3MiOiJkaXJlY3R1cyJ9.UmDChpEUwKyYEdnaBSq7k7rdI5OYvw3azS1bADxGNkU"

    suspend fun getExerciseById(exerciseId: String): Response<Exercise> {
        return RetrofitInstance.exerciseService.getExerciseById(
            exerciseId = exerciseId,
            token = jwtToken
        )
    }

    suspend fun getFirstExercise(): Exercise? {
        val response = RetrofitInstance.exerciseService.getExercises(token = jwtToken)
        return if (response.isSuccessful) response.body()?.data?.firstOrNull() else null
    }
}
