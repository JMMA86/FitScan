package icesi.edu.co.fitscan.features.workout.domain.service.impl

import android.util.Log
import icesi.edu.co.fitscan.features.common.ui.viewmodel.AppState
import icesi.edu.co.fitscan.features.workout.domain.data.remote.response.Workout
import icesi.edu.co.fitscan.features.workout.domain.data.remote.response.WorkoutResponse
import icesi.edu.co.fitscan.features.workout.domain.repository.WorkoutListRepository
import icesi.edu.co.fitscan.features.workout.domain.service.WorkoutListService

class WorkoutListServiceImpl(
    private val repository: WorkoutListRepository
) : WorkoutListService {

    override suspend fun getWorkouts(): List<Workout> {
        val token = AppState.token ?: throw Exception("No hay token de autenticación")
        val customerId = AppState.customerId ?: throw Exception("No hay ID de usuario")
        
        Log.d("WorkoutListServiceImpl", "Token: $token")
        Log.d("WorkoutListServiceImpl", "CustomerId: $customerId")
        Log.d("WorkoutListServiceImpl", "URL: items/workout?filter[customer_id][_eq]=$customerId")
        
        val response = repository.getWorkouts("Bearer $token", customerId)
        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string() ?: "No error body"
            Log.e("WorkoutListServiceImpl", "Error response: $errorBody")
            Log.e("WorkoutListServiceImpl", "Error code: ${response.code()}")
            Log.e("WorkoutListServiceImpl", "Error message: ${response.message()}")
            throw Exception("Error al obtener los entrenamientos: ${response.code()}")
        }
        
        val workouts = response.body()?.data ?: emptyList()
        Log.d("WorkoutListServiceImpl", "Workouts received: ${workouts.size}")
        workouts.forEach { workout ->
            Log.d("WorkoutListServiceImpl", "Workout: id=${workout.id}, name=${workout.name}")
        }
        return workouts
    }
} 