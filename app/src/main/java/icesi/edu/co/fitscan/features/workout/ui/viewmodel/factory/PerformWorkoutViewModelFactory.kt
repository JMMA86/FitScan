package icesi.edu.co.fitscan.features.workout.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import icesi.edu.co.fitscan.features.common.data.remote.RetrofitInstance
import icesi.edu.co.fitscan.features.workout.data.dataSources.WorkoutDataSource
import icesi.edu.co.fitscan.features.workout.data.mapper.WorkoutMapper
import icesi.edu.co.fitscan.features.workout.data.repositories.WorkoutRepository
import icesi.edu.co.fitscan.features.workout.data.repositories.impl.WorkoutRepositoryImpl
import icesi.edu.co.fitscan.features.workout.domain.usecase.PerformWorkoutUseCase
import icesi.edu.co.fitscan.features.workout.ui.viewmodel.PerformWorkoutViewModel

class PerformWorkoutViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PerformWorkoutViewModel::class.java)) {
            val workoutDataSource = RetrofitInstance.create(WorkoutDataSource::class.java)
            val workoutMapper = WorkoutMapper()
            val workoutRepository: WorkoutRepository = WorkoutRepositoryImpl(
                datasource = workoutDataSource,
                mapper = workoutMapper
            )
            val performWorkoutUseCase = PerformWorkoutUseCase(workoutRepository)

            @Suppress("UNCHECKED_CAST")
            return PerformWorkoutViewModel(
                performWorkoutUseCase = performWorkoutUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}