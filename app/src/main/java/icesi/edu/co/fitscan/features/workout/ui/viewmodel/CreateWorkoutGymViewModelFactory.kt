package icesi.edu.co.fitscan.features.workout.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import icesi.edu.co.fitscan.features.common.data.remote.RetrofitInstance
import icesi.edu.co.fitscan.features.workout.data.api.ExerciseApiService
import icesi.edu.co.fitscan.features.workout.data.dataSources.ExerciseService
import icesi.edu.co.fitscan.features.workout.data.dataSources.impl.ExerciseServiceImpl
import icesi.edu.co.fitscan.features.workout.data.repositories.ExerciseRepository
import icesi.edu.co.fitscan.features.workout.data.repositories.impl.ExerciseRepositoryImpl
import icesi.edu.co.fitscan.features.workout.domain.mapper.ExerciseMapper
import icesi.edu.co.fitscan.features.workout.domain.usecase.GetExercisesUseCase

class CreateWorkoutGymViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateWorkoutGymViewModel::class.java)) {
            // Obtener la instancia de la API a través de Retrofit
            val apiService = RetrofitInstance.create(ExerciseApiService::class.java)
            val mapper = ExerciseMapper()
            
            val repository: ExerciseRepository = ExerciseRepositoryImpl(
                api = apiService,
                mapper = mapper
            )
            val service: ExerciseService = ExerciseServiceImpl(repository)
            val getExercisesUseCase = GetExercisesUseCase(service)
            
            @Suppress("UNCHECKED_CAST")
            return CreateWorkoutGymViewModel(getExercisesUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
