package icesi.edu.co.fitscan.features.workout.data.remote

import com.google.gson.annotations.SerializedName
import icesi.edu.co.fitscan.features.workout.data.model.Exercise
import icesi.edu.co.fitscan.features.workout.data.model.Workout
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface WorkoutService {
    // Consulta una rutina específica por ID, expandiendo los ejercicios relacionados
    @GET("items/workout")
    suspend fun getWorkouts(
        @Header("Authorization") token: String,
        @Query("fields") fields: String = "*,workout_exercise.*,workout_exercise.exercise_id.*",
        @Query("limit") limit: Int = 1
    ): Response<DirectusListResponse<Workout>>
}

interface ExerciseService {
    @GET("items/exercise/{id}")
    suspend fun getExerciseById(
        @Path("id") exerciseId: String,
        @Header("Authorization") token: String
    ): Response<Exercise>

    @GET("items/exercise")
    suspend fun getExercises(
        @Header("Authorization") token: String,
        @Query("fields") fields: String = "*",
        @Query("limit") limit: Int = 1
    ): Response<DirectusListResponse<Exercise>>
}

data class DirectusListResponse<T>(
    @SerializedName("data") val data: List<T>
)