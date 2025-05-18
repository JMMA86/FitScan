package icesi.edu.co.fitscan.features.workout.data.repository

import icesi.edu.co.fitscan.features.common.data.remote.RetrofitInstance
import icesi.edu.co.fitscan.features.workout.data.model.Workout
import icesi.edu.co.fitscan.features.workout.data.model.WorkoutExerciseWithExercise
import retrofit2.Response

class WorkoutRepository {
    // Token JWT HARDCODEADO para pruebas (pon aquí el tuyo de Postman)
    private val jwtToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6ImZmNzAzNzZjLWMzODktNGIzNS05NGM2LTkzOTBkZWUxZGU4OCIsInJvbGUiOiIzYWFmYTc5My1kNmZmLTRiNDAtYjYyMC04YWNmYjM1NjgwNWUiLCJhcHBfYWNjZXNzIjp0cnVlLCJhZG1pbl9hY2Nlc3MiOnRydWUsImlhdCI6MTc0NzYwNzEwMSwiZXhwIjoxNzQ3NjEwNzAxLCJpc3MiOiJkaXJlY3R1cyJ9.Eg_2oD24r6mtfPrcoHH4eD1MyvF3JEcfk6Jjeijt3vg"

    // Si quieres seguir permitiendo buscar por ID, descomenta y usa este método:
    suspend fun getWorkoutById(workoutId: String): Response<Workout> {
        return RetrofitInstance.workoutService.getWorkoutById(
            workoutId = workoutId,
            token = jwtToken
        )
    }

    suspend fun getFirstWorkout(): Workout? {
        val response = RetrofitInstance.workoutService.getWorkouts(token = jwtToken)
        return if (response.isSuccessful) response.body()?.data?.firstOrNull() else null
    }

    suspend fun getWorkoutExercisesByWorkoutId(workoutId: String): List<WorkoutExerciseWithExercise> {
        val response = RetrofitInstance.workoutService.getWorkoutExercisesByWorkoutId(
            token = jwtToken,
            workoutId = workoutId
        )
        return if (response.isSuccessful) response.body()?.data ?: emptyList() else emptyList()
    }
}
