package icesi.edu.co.fitscan.features.workout.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import icesi.edu.co.fitscan.features.common.data.remote.RetrofitInstance
import icesi.edu.co.fitscan.features.workout.data.dataSources.ExerciseDataSource
import icesi.edu.co.fitscan.features.workout.data.dataSources.WorkoutDataSource
import icesi.edu.co.fitscan.features.workout.data.dataSources.WorkoutExerciseDataSource
import icesi.edu.co.fitscan.features.workout.data.repositories.ExerciseRepository
import icesi.edu.co.fitscan.features.workout.data.repositories.WorkoutExerciseRepository
import icesi.edu.co.fitscan.features.workout.data.repositories.WorkoutRepository
import icesi.edu.co.fitscan.features.workout.data.repositories.impl.ExerciseRepositoryImpl
import icesi.edu.co.fitscan.features.workout.data.repositories.impl.WorkoutExerciseRepositoryImpl
import icesi.edu.co.fitscan.features.workout.data.repositories.impl.WorkoutRepositoryImpl
import icesi.edu.co.fitscan.features.workout.data.mapper.ExerciseMapper
import icesi.edu.co.fitscan.features.workout.data.mapper.WorkoutExerciseMapper
import icesi.edu.co.fitscan.features.workout.data.mapper.WorkoutMapper
import icesi.edu.co.fitscan.features.workout.domain.usecase.CreateWorkoutUseCase
import icesi.edu.co.fitscan.features.workout.domain.usecase.GetExercisesUseCase
import icesi.edu.co.fitscan.features.workout.ui.viewmodel.CreateWorkoutGymViewModel

class CreateWorkoutGymViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateWorkoutGymViewModel::class.java)) {
            val exerciseDataSource = RetrofitInstance.create(ExerciseDataSource::class.java)
            val workoutDataSource = RetrofitInstance.create(WorkoutDataSource::class.java)
            val workoutExerciseDataSource = RetrofitInstance.create(WorkoutExerciseDataSource::class.java)

            val exerciseMapper = ExerciseMapper()
            val workoutMapper = WorkoutMapper()
            val workoutExerciseMapper = WorkoutExerciseMapper()

            val exerciseRepository: ExerciseRepository = ExerciseRepositoryImpl(
                datasource = exerciseDataSource,
                mapper = exerciseMapper
            )
            val workoutRepository: WorkoutRepository = WorkoutRepositoryImpl(
                datasource = workoutDataSource,
                mapper = workoutMapper
            )
            val workoutExerciseRepository: WorkoutExerciseRepository = WorkoutExerciseRepositoryImpl(
                datasource = workoutExerciseDataSource,
                mapper = workoutExerciseMapper
            )

            val getExercisesUseCase = GetExercisesUseCase(exerciseRepository)
            val createWorkoutUseCase = CreateWorkoutUseCase(workoutRepository, workoutExerciseRepository)
            
            @Suppress("UNCHECKED_CAST")
            return CreateWorkoutGymViewModel(
                getExercisesUseCase = getExercisesUseCase,
                createWorkoutUseCase = createWorkoutUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
