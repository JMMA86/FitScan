package icesi.edu.co.fitscan.features.workout.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import icesi.edu.co.fitscan.domain.repositories.IExerciseRepository
import icesi.edu.co.fitscan.domain.repositories.IWorkoutExerciseRepository
import icesi.edu.co.fitscan.domain.repositories.IWorkoutRepository
import icesi.edu.co.fitscan.domain.usecases.ICreateWorkoutUseCase
import icesi.edu.co.fitscan.domain.usecases.IGetExercisesUseCase
import icesi.edu.co.fitscan.domain.usecases.ICreateExerciseUseCase
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
import icesi.edu.co.fitscan.features.workout.data.usecases.CreateWorkoutUseCaseImpl
import icesi.edu.co.fitscan.features.workout.data.usecases.GetExercisesUseCaseImpl
import icesi.edu.co.fitscan.features.workout.data.usecases.CreateExerciseUseCaseImpl
import icesi.edu.co.fitscan.features.workout.ui.viewmodel.CreateWorkoutGymViewModel

class CreateWorkoutGymViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateWorkoutGymViewModel::class.java)) {
            val exerciseDataSource = RetrofitInstance.create(IExerciseDataSource::class.java)
            val workoutDataSource = RetrofitInstance.create(IWorkoutDataSource::class.java)
            val workoutExerciseDataSource = RetrofitInstance.create(IWorkoutExerciseDataSource::class.java)

            val exerciseMapper = ExerciseMapper()
            val workoutMapper = WorkoutMapper()
            val workoutExerciseMapper = WorkoutExerciseMapper()

            val exerciseRepository: IExerciseRepository = ExerciseRepositoryImpl(
                datasource = exerciseDataSource,
                mapper = exerciseMapper
            )
            val workoutRepository: IWorkoutRepository = WorkoutRepositoryImpl(
                datasource = workoutDataSource,
                mapper = workoutMapper
            )
            val workoutExerciseRepository: IWorkoutExerciseRepository = WorkoutExerciseRepositoryImpl(
                datasource = workoutExerciseDataSource,
                mapper = workoutExerciseMapper
            )

            val getExercisesUseCase: IGetExercisesUseCase = GetExercisesUseCaseImpl(exerciseRepository)
            val createWorkoutUseCase: ICreateWorkoutUseCase = CreateWorkoutUseCaseImpl(workoutRepository, workoutExerciseRepository)
            val createExerciseUseCase: ICreateExerciseUseCase = CreateExerciseUseCaseImpl(exerciseRepository)
            
            @Suppress("UNCHECKED_CAST")
            return CreateWorkoutGymViewModel(
                getExercisesUseCase = getExercisesUseCase,
                createWorkoutUseCase = createWorkoutUseCase,
                createExerciseUseCase = createExerciseUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
