package icesi.edu.co.fitscan.features.workoutlist.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import icesi.edu.co.fitscan.domain.repositories.IWorkoutRepository
import icesi.edu.co.fitscan.domain.usecases.IGetWorkoutsUseCase
import icesi.edu.co.fitscan.features.common.data.remote.RetrofitInstance
import icesi.edu.co.fitscan.features.workout.data.dataSources.IWorkoutDataSource
import icesi.edu.co.fitscan.features.workout.data.mapper.WorkoutMapper
import icesi.edu.co.fitscan.features.workout.data.repositories.WorkoutRepositoryImpl
import icesi.edu.co.fitscan.features.workoutlist.data.usecases.GetWorkoutsUseCaseImpl
import icesi.edu.co.fitscan.features.workoutlist.ui.viewmodel.WorkoutListViewModel

class WorkoutListViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkoutListViewModel::class.java)) {
            val workoutDataSource = RetrofitInstance.create(IWorkoutDataSource::class.java)
            val workoutMapper = WorkoutMapper()
            val workoutRepository: IWorkoutRepository = WorkoutRepositoryImpl(
                datasource = workoutDataSource,
                mapper = workoutMapper
            )
            val getWorkoutsUseCase: IGetWorkoutsUseCase = GetWorkoutsUseCaseImpl(workoutRepository)
            @Suppress("UNCHECKED_CAST")
            return WorkoutListViewModel(getWorkoutsUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}