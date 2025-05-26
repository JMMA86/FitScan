package icesi.edu.co.fitscan.features.workout.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import icesi.edu.co.fitscan.domain.repositories.IExerciseRepository
import icesi.edu.co.fitscan.domain.repositories.IWorkoutExerciseRepository
import icesi.edu.co.fitscan.features.common.data.remote.RetrofitInstance
import icesi.edu.co.fitscan.features.workout.data.dataSources.IExerciseDataSource
import icesi.edu.co.fitscan.features.workout.data.dataSources.IWorkoutExerciseDataSource
import icesi.edu.co.fitscan.features.workout.data.mapper.ExerciseMapper
import icesi.edu.co.fitscan.features.workout.data.mapper.WorkoutExerciseMapper
import icesi.edu.co.fitscan.features.workout.data.repositories.ExerciseRepositoryImpl
import icesi.edu.co.fitscan.features.workout.data.repositories.WorkoutExerciseRepositoryImpl
import icesi.edu.co.fitscan.features.workout.ui.viewmodel.ExerciseDetailViewModel

class ExerciseDetailViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExerciseDetailViewModel::class.java)) {
            val exerciseDataSource = RetrofitInstance.create(IExerciseDataSource::class.java)
            val workoutExerciseDataSource = RetrofitInstance.create(IWorkoutExerciseDataSource::class.java)
            val exerciseMapper = ExerciseMapper()
            val workoutExerciseMapper = WorkoutExerciseMapper()
            val exerciseRepository: IExerciseRepository = ExerciseRepositoryImpl(
                datasource = exerciseDataSource,
                mapper = exerciseMapper
            )
            val workoutExerciseRepository: IWorkoutExerciseRepository = WorkoutExerciseRepositoryImpl(
                datasource = workoutExerciseDataSource,
                mapper = workoutExerciseMapper
            )
            @Suppress("UNCHECKED_CAST")
            return ExerciseDetailViewModel(
                exerciseRepository = exerciseRepository,
                workoutExerciseRepository = workoutExerciseRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
