package icesi.edu.co.fitscan.features.workout.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import icesi.edu.co.fitscan.domain.repositories.IExerciseRepository
import icesi.edu.co.fitscan.domain.repositories.IWorkoutExerciseRepository
import icesi.edu.co.fitscan.domain.repositories.IWorkoutRepository
import icesi.edu.co.fitscan.features.common.data.remote.RetrofitInstance
import icesi.edu.co.fitscan.features.workout.data.dataSources.IExerciseDataSource
import icesi.edu.co.fitscan.features.workout.data.dataSources.IWorkoutDataSource
import icesi.edu.co.fitscan.features.workout.data.dataSources.IWorkoutExerciseDataSource
import icesi.edu.co.fitscan.features.workout.data.mapper.ExerciseMapper
import icesi.edu.co.fitscan.features.workout.data.mapper.WorkoutExerciseMapper
import icesi.edu.co.fitscan.features.workout.data.mapper.WorkoutMapper
import icesi.edu.co.fitscan.features.workout.data.repositories.ExerciseRepositoryImpl
import icesi.edu.co.fitscan.features.workout.data.repositories.WorkoutExerciseRepositoryImpl
import icesi.edu.co.fitscan.features.workout.data.repositories.WorkoutRepositoryImpl
import icesi.edu.co.fitscan.features.workout.ui.viewmodel.WorkoutDetailViewModel

class WorkoutDetailViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkoutDetailViewModel::class.java)) {
            val workoutDataSource = RetrofitInstance.create(IWorkoutDataSource::class.java)
            val workoutExerciseDataSource = RetrofitInstance.create(IWorkoutExerciseDataSource::class.java)
            val exerciseDataSource = RetrofitInstance.create(IExerciseDataSource::class.java)

            val workoutMapper = WorkoutMapper()
            val workoutExerciseMapper = WorkoutExerciseMapper()
            val exerciseMapper = ExerciseMapper()

            val workoutRepository: IWorkoutRepository = WorkoutRepositoryImpl(
                datasource = workoutDataSource,
                mapper = workoutMapper
            )
            val workoutExerciseRepository: IWorkoutExerciseRepository = WorkoutExerciseRepositoryImpl(
                datasource = workoutExerciseDataSource,
                mapper = workoutExerciseMapper
            )
            val exerciseRepository: IExerciseRepository = ExerciseRepositoryImpl(
                datasource = exerciseDataSource,
                mapper = exerciseMapper
            )

            @Suppress("UNCHECKED_CAST")
            return WorkoutDetailViewModel(
                workoutRepository = workoutRepository,
                workoutExerciseRepository = workoutExerciseRepository,
                exerciseRepository = exerciseRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
