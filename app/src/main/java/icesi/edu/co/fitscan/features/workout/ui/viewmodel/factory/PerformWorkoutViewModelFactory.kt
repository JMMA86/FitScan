package icesi.edu.co.fitscan.features.workout.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import icesi.edu.co.fitscan.domain.repositories.IWorkoutExerciseRepository
import icesi.edu.co.fitscan.features.common.data.remote.RetrofitInstance
import icesi.edu.co.fitscan.features.workout.data.dataSources.IWorkoutExerciseDataSource
import icesi.edu.co.fitscan.features.workout.data.mapper.WorkoutExerciseMapper
import icesi.edu.co.fitscan.features.workout.data.repositories.WorkoutExerciseRepositoryImpl
import icesi.edu.co.fitscan.features.workout.data.usecases.ManageWorkoutExercisesUseCaseImpl
import icesi.edu.co.fitscan.features.workout.ui.viewmodel.PerformWorkoutViewModel

class PerformWorkoutViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PerformWorkoutViewModel::class.java)) {
            val workoutDataSource = RetrofitInstance.create(IWorkoutExerciseDataSource::class.java)
            val workoutMapper = WorkoutExerciseMapper()
            val workoutRepository: IWorkoutExerciseRepository = WorkoutExerciseRepositoryImpl(
                datasource = workoutDataSource,
                mapper = workoutMapper
            )
            val performWorkoutUseCase = ManageWorkoutExercisesUseCaseImpl(workoutRepository)

            @Suppress("UNCHECKED_CAST")
            return PerformWorkoutViewModel(
                performWorkoutUseCase = performWorkoutUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}