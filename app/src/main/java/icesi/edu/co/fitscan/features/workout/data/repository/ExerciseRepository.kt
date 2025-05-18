package icesi.edu.co.fitscan.features.workout.data.repository

import com.google.gson.annotations.SerializedName
import icesi.edu.co.fitscan.features.common.data.remote.RetrofitInstance
import icesi.edu.co.fitscan.features.workout.data.model.Exercise
import icesi.edu.co.fitscan.features.workout.data.model.WorkoutExerciseWithExercise
import retrofit2.Response

// Wrapper para respuestas de objeto único de Directus
// (Si ya existe en otro archivo, puedes eliminar esta definición)
data class DirectusObjectResponse<T>(
    @SerializedName("data") val data: T
)

class ExerciseRepository {
    // Usa el mismo token JWT que en WorkoutRepository
    private val jwtToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6ImZmNzAzNzZjLWMzODktNGIzNS05NGM2LTkzOTBkZWUxZGU4OCIsInJvbGUiOiIzYWFmYTc5My1kNmZmLTRiNDAtYjYyMC04YWNmYjM1NjgwNWUiLCJhcHBfYWNjZXNzIjp0cnVlLCJhZG1pbl9hY2Nlc3MiOnRydWUsImlhdCI6MTc0NzYxMTExMywiZXhwIjoxNzQ3NjE0NzEzLCJpc3MiOiJkaXJlY3R1cyJ9.3muW5_JOrMf954Q2Zekx5Fs3UWke72Vn0bUPT1brT60"

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

    suspend fun getWorkoutExerciseById(workoutExerciseId: String): WorkoutExerciseWithExercise? {
        val response = RetrofitInstance.workoutService.getWorkoutExerciseById(
            workoutExerciseId = workoutExerciseId,
            token = jwtToken
        )
        // Cambia para extraer el campo 'data' del wrapper
        return if (response.isSuccessful) response.body()?.data else null
    }
}
