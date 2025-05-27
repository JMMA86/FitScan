package icesi.edu.co.fitscan.features.workout.data.repositories

import android.util.Log
import icesi.edu.co.fitscan.domain.model.WorkoutSession
import icesi.edu.co.fitscan.features.workout.data.dataSources.IWorkoutSessionDataSource
import icesi.edu.co.fitscan.features.workout.data.dto.WorkoutSessionDto
import icesi.edu.co.fitscan.features.workout.data.mapper.WorkoutSessionMapper
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response


class WorkoutSessionRepositoryImpl(
    private val workoutSessionDataSource: IWorkoutSessionDataSource,
    private val mapper: WorkoutSessionMapper
) {
    suspend fun createWorkoutSession(workoutSession: WorkoutSession): Response<WorkoutSessionDto>? {
        if (workoutSession.workoutId.isBlank()) {
            throw IllegalArgumentException("Workout ID is required")
        }

        if (workoutSession.customerId.isBlank()) {
            throw IllegalArgumentException("User ID is required")
        }

        return try {
            val workoutSessionDto = mapper.toDto(workoutSession)
//            val apiRequest = ApiResponseDTO(
//                data = workoutSessionDto
//            )
            workoutSessionDataSource.createWorkoutSession(workoutSessionDto).let { response ->
                if (response.isSuccessful) {
                    response.body()?.data?.let { workoutSessionDto ->
                        Response.success(workoutSessionDto)
                    } ?: Response.error(404, "Workout session not found".toResponseBody())
                } else {
                    Response.error(
                        response.code(),
                        response.errorBody() ?: "Unknown error".toResponseBody()
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("WorkoutSessionRepository", "Error creating workout session: ${e.message}")
            null
        }
    }
}