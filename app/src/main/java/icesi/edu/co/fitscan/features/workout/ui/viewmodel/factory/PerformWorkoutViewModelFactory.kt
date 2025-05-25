package icesi.edu.co.fitscan.features.workout.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import icesi.edu.co.fitscan.domain.repositories.IExerciseRepository
import icesi.edu.co.fitscan.domain.repositories.IWorkoutExerciseRepository
import icesi.edu.co.fitscan.domain.repositories.IWorkoutRepository
import icesi.edu.co.fitscan.domain.usecases.IManageExercisesUseCase
import icesi.edu.co.fitscan.domain.usecases.IManageWorkoutExercisesUseCase
import icesi.edu.co.fitscan.domain.usecases.IManageWorkoutUseCase
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
import icesi.edu.co.fitscan.features.workout.data.usecases.ManageExercisesUseCaseImpl
import icesi.edu.co.fitscan.features.workout.data.usecases.ManageWorkoutExercisesUseCaseImpl
import icesi.edu.co.fitscan.features.workout.data.usecases.ManageWorkoutUseCaseImpl
import icesi.edu.co.fitscan.features.workout.ui.viewmodel.PerformWorkoutViewModel

class PerformWorkoutViewModelFactory(
    private val workoutSessionId: String,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PerformWorkoutViewModel::class.java)) {
            val workoutExerciseDataSource =
                RetrofitInstance.create(IWorkoutExerciseDataSource::class.java)
            val workoutExerciseMapper = WorkoutExerciseMapper()
            val workoutExerciseRepository: IWorkoutExerciseRepository =
                WorkoutExerciseRepositoryImpl(
                    datasource = workoutExerciseDataSource,
                    mapper = workoutExerciseMapper
                )
            val workoutExerciseUseCase =
                ManageWorkoutExercisesUseCaseImpl(workoutExerciseRepository) as IManageWorkoutExercisesUseCase

            val exerciseDataSource = RetrofitInstance.create(IExerciseDataSource::class.java)
            val exerciseMapper = ExerciseMapper()
            val exerciseRepository: IExerciseRepository = ExerciseRepositoryImpl(
                datasource = exerciseDataSource,
                mapper = exerciseMapper
            )
            val exerciseUseCase =
                ManageExercisesUseCaseImpl(exerciseRepository) as IManageExercisesUseCase

            val workoutDataSource = RetrofitInstance.create(IWorkoutDataSource::class.java)
            val workoutMapper = WorkoutMapper()
            val workoutRepository: IWorkoutRepository = WorkoutRepositoryImpl(
                datasource = workoutDataSource,
                mapper = workoutMapper
            )
            val workoutUseCase =
                ManageWorkoutUseCaseImpl(
                    workoutRepository,
                    workoutExerciseRepository
                ) as IManageWorkoutUseCase

            @Suppress("UNCHECKED_CAST")
            return PerformWorkoutViewModel(
                performWorkoutUseCase = workoutExerciseUseCase,
                exerciseUseCase = exerciseUseCase,
                workoutUseCase = workoutUseCase,
                workoutSessionId = workoutSessionId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}