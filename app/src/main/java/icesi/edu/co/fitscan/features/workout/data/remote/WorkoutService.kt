package icesi.edu.co.fitscan.features.workout.data.remote

import com.google.gson.annotations.SerializedName
import icesi.edu.co.fitscan.features.workout.data.model.Exercise
import icesi.edu.co.fitscan.features.workout.data.model.Workout
import icesi.edu.co.fitscan.features.workout.data.model.WorkoutExerciseWithExercise
import icesi.edu.co.fitscan.features.workout.data.model.DirectusListResponse
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

    @GET("items/workout/{id}")
    suspend fun getWorkoutById(
        @Path("id") workoutId: String,
        @Header("Authorization") token: String,
        @Query("fields") fields: String = "*"
    ): Response<Workout>

    @GET("items/workout_exercise")
    suspend fun getWorkoutExercisesByWorkoutId(
        @Header("Authorization") token: String,
        @Query("filter[workout_id][_eq]") workoutId: String,
        @Query("fields") fields: String = "*,exercise_id.*"
    ): Response<DirectusListResponse<WorkoutExerciseWithExercise>>

    @GET("items/workout_exercise/{id}")
    suspend fun getWorkoutExerciseById(
        @Path("id") workoutExerciseId: String,
        @Header("Authorization") token: String,
        @Query("fields") fields: String = "*,exercise_id.*"
    ): Response<WorkoutExerciseWithExercise>
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