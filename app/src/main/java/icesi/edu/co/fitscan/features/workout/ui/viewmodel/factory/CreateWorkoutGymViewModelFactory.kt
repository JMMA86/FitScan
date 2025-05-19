package icesi.edu.co.fitscan.features.workout.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import icesi.edu.co.fitscan.features.common.data.remote.RetrofitInstance
import icesi.edu.co.fitscan.features.workout.data.api.ExerciseApiService
import icesi.edu.co.fitscan.features.workout.data.api.WorkoutApiService
import icesi.edu.co.fitscan.features.workout.data.api.WorkoutExerciseApiService
import icesi.edu.co.fitscan.features.workout.data.dataSources.ExerciseService
import icesi.edu.co.fitscan.features.workout.data.dataSources.WorkoutExerciseService
import icesi.edu.co.fitscan.features.workout.data.dataSources.WorkoutService
import icesi.edu.co.fitscan.features.workout.data.dataSources.impl.ExerciseServiceImpl
import icesi.edu.co.fitscan.features.workout.data.dataSources.impl.WorkoutExerciseServiceImpl
import icesi.edu.co.fitscan.features.workout.data.dataSources.impl.WorkoutServiceImpl
import icesi.edu.co.fitscan.features.workout.data.repositories.ExerciseRepository
import icesi.edu.co.fitscan.features.workout.data.repositories.WorkoutExerciseRepository
import icesi.edu.co.fitscan.features.workout.data.repositories.WorkoutRepository
import icesi.edu.co.fitscan.features.workout.data.repositories.impl.ExerciseRepositoryImpl
import icesi.edu.co.fitscan.features.workout.data.repositories.impl.WorkoutExerciseRepositoryImpl
import icesi.edu.co.fitscan.features.workout.data.repositories.impl.WorkoutRepositoryImpl
import icesi.edu.co.fitscan.features.workout.domain.mapper.ExerciseMapper
import icesi.edu.co.fitscan.features.workout.domain.mapper.WorkoutExerciseMapper
import icesi.edu.co.fitscan.features.workout.domain.mapper.WorkoutMapper
import icesi.edu.co.fitscan.features.workout.domain.usecase.CreateWorkoutUseCase
import icesi.edu.co.fitscan.features.workout.domain.usecase.GetExercisesUseCase
import icesi.edu.co.fitscan.features.workout.ui.viewmodel.CreateWorkoutGymViewModel

class CreateWorkoutGymViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateWorkoutGymViewModel::class.java)) {
            val exerciseApiService = RetrofitInstance.create(ExerciseApiService::class.java)
            val workoutApiService = RetrofitInstance.create(WorkoutApiService::class.java)
            val workoutExerciseApiService = RetrofitInstance.create(WorkoutExerciseApiService::class.java)

            val exerciseMapper = ExerciseMapper()
            val workoutMapper = WorkoutMapper()
            val workoutExerciseMapper = WorkoutExerciseMapper()

            val exerciseRepository: ExerciseRepository = ExerciseRepositoryImpl(
                api = exerciseApiService,
                mapper = exerciseMapper
            )
            val workoutRepository: WorkoutRepository = WorkoutRepositoryImpl(
                api = workoutApiService,
                mapper = workoutMapper
            )
            val workoutExerciseRepository: WorkoutExerciseRepository = WorkoutExerciseRepositoryImpl(
                api = workoutExerciseApiService,
                mapper = workoutExerciseMapper
            )

            val exerciseService: ExerciseService = ExerciseServiceImpl(exerciseRepository)
            val workoutService: WorkoutService = WorkoutServiceImpl(workoutRepository, workoutExerciseRepository)
            val workoutExerciseService: WorkoutExerciseService = WorkoutExerciseServiceImpl(workoutExerciseRepository)

            val getExercisesUseCase = GetExercisesUseCase(exerciseService)
            val createWorkoutUseCase = CreateWorkoutUseCase(workoutService, workoutExerciseService)
            
            @Suppress("UNCHECKED_CAST")
            return CreateWorkoutGymViewModel(
                getExercisesUseCase = getExercisesUseCase,
                createWorkoutUseCase = createWorkoutUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
