package icesi.edu.co.fitscan.features.workout.domain.service.impl

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
        
        val response = repository.getWorkouts(token, customerId)
        if (!response.isSuccessful) {
            throw Exception("Error al obtener los entrenamientos: ${response.code()}")
        }
        return response.body()?.data ?: throw Exception("Respuesta vacía del servidor")
    }
} 