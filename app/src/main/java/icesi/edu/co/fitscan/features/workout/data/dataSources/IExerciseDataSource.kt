package icesi.edu.co.fitscan.features.workout.data.dataSources

import icesi.edu.co.fitscan.features.workout.data.dto.ExerciseDto
import icesi.edu.co.fitscan.features.workout.data.dto.ExerciseResponseDto
import icesi.edu.co.fitscan.features.workout.data.dto.ExerciseSingleResponseDto
import retrofit2.Response
import retrofit2.http.*

@JvmSuppressWildcards
interface IExerciseDataSource {

    companion object {
        const val BASE_PATH = "items"
    }    @GET("$BASE_PATH/exercise")
    suspend fun getAllExercises(
        @Query("fields") fields: String = "*,primary_muscle_group_id.*,secondary_muscle_groups.*,secondary_muscle_groups.muscle_group.*"
    ): Response<ExerciseResponseDto>    @GET("$BASE_PATH/exercise/{id}")
    suspend fun getExerciseById(
        @Path("id") id: String,
        @Query("fields") fields: String = "*,primary_muscle_group_id.*,secondary_muscle_groups.*,secondary_muscle_groups.muscle_group.*"
    ): Response<ExerciseSingleResponseDto>

    @POST("$BASE_PATH/exercise")
    suspend fun createExercise(@Body exercise: ExerciseDto): Response<ExerciseDto>

    @PUT("$BASE_PATH/exercise/{id}")
    suspend fun updateExercise(
        @Path("id") id: String,
        @Body exercise: ExerciseDto
    ): Response<ExerciseDto>

    @DELETE("$BASE_PATH/exercise/{id}")
    suspend fun deleteExercise(@Path("id") id: String): Response<Unit>

}

