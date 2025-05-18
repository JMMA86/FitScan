package icesi.edu.co.fitscan.features.workout.data.api

import icesi.edu.co.fitscan.features.workout.data.dto.ExerciseDto
import retrofit2.Response
import retrofit2.http.*

@JvmSuppressWildcards
interface ExerciseApiService {

    companion object {
        const val BASE_PATH = "items"
    }

    @GET("$BASE_PATH/exercises")
    suspend fun getAllExercises(): Response<List<ExerciseDto>>

    @GET("$BASE_PATH/exercises/{id}")
    suspend fun getExerciseById(@Path("id") id: String): Response<ExerciseDto>

    @POST("$BASE_PATH/exercises")
    suspend fun createExercise(@Body exercise: ExerciseDto): Response<ExerciseDto>

    @PUT("$BASE_PATH/exercises/{id}")
    suspend fun updateExercise(
        @Path("id") id: String,
        @Body exercise: ExerciseDto
    ): Response<ExerciseDto>

    @DELETE("$BASE_PATH/exercises/{id}")
    suspend fun deleteExercise(@Path("id") id: String): Response<Unit>

}

