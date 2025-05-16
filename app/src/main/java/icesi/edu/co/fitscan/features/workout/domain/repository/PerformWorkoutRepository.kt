package icesi.edu.co.fitscan.features.workout.domain.repository

import icesi.edu.co.fitscan.features.workout.domain.data.remote.response.WorkoutExerciseResponse
import icesi.edu.co.fitscan.features.workout.domain.data.remote.response.WorkoutResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface PerformWorkoutRepository {
    @GET("/items/workout/{customerId}")
    suspend fun getWorkout(
        @Header("Authorization") token: String,
        @retrofit2.http.Path("customerId") customerId: String
    ): Response<WorkoutResponse>

    @GET("/items/workout_exercise/")
    suspend fun getWorkoutExercises(
        @Header("Authorization") token: String,
        @Query("filter[workout_id][_eq]") workoutId: String
    ): Response<WorkoutExerciseResponse>
}