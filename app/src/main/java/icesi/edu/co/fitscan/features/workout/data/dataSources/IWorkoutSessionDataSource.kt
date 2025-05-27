package icesi.edu.co.fitscan.features.workout.data.dataSources

import icesi.edu.co.fitscan.features.workout.data.dto.ApiResponseDTO
import icesi.edu.co.fitscan.features.workout.data.dto.WorkoutSessionDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface IWorkoutSessionDataSource {
    //TODO: Change the name of APIResponse to something more descriptive
    @POST("/items/workout_session")
    suspend fun createWorkoutSession(
        @Body workoutSession: WorkoutSessionDto
    ): Response<ApiResponseDTO<WorkoutSessionDto>>
}