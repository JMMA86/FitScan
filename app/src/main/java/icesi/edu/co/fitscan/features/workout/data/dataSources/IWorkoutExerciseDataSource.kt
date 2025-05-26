package icesi.edu.co.fitscan.features.workout.data.dataSources

import icesi.edu.co.fitscan.features.workout.data.dto.WorkoutExerciseDto
import icesi.edu.co.fitscan.features.workout.data.dto.WorkoutExerciseResponseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

@JvmSuppressWildcards
interface IWorkoutExerciseDataSource {

    companion object {
        const val BASE_PATH = "items"
    }

    @GET("$BASE_PATH/workout_exercise")
    suspend fun getExercisesByWorkoutId(
        @Query("filter[workout_id][_eq]") workout_id: String
    ): Response<WorkoutExerciseResponseDTO>

    @GET("$BASE_PATH/workout_exercise/{workout_id}/exercise/{exercise_id}")
    suspend fun getWorkoutExercisesById(
        @Path("workout_id") workout_id: String,
        @Path("exercise_id") exercise_id: String
    ): Response<WorkoutExerciseDto>

    @POST("$BASE_PATH/workout_exercise")
    suspend fun addExerciseToWorkout(
        @Body workoutExercise: WorkoutExerciseDto
    ): Response<WorkoutExerciseDto>

    @PUT("$BASE_PATH/workout_exercise/{workout_id}/exercise/{exercise_id}")
    suspend fun updateWorkoutExercise(
        @Path("workout_id") workout_id: String,
        @Path("exercise_id") exercise_id: String,
        @Body workoutExercise: WorkoutExerciseDto
    ): Response<WorkoutExerciseDto>

    @DELETE("$BASE_PATH/workout_exercise/{workout_id}/exercise/{exercise_id}")
    suspend fun removeExerciseFromWorkout(
        @Path("workout_id") workout_id: String,
        @Path("exercise_id") exercise_id: String
    ): Response<Unit>

    @PUT("$BASE_PATH/workout_exercise/{workout_id}/exercise/reorder")
    suspend fun reorderWorkoutExercise(
        @Path("workout_id") workout_id: String,
        @Body exerciseOrder: List<String>
    ): Response<List<WorkoutExerciseDto>>

    @PATCH("$BASE_PATH/workout_exercise/{workoutId}/exercise/{exercise_id}/complete")
    suspend fun markExerciseAsCompleted(
        @Path("workout_id") workout_id: String,
        @Path("exercise_id") exercise_id: String
    ): Response<WorkoutExerciseDto>
}