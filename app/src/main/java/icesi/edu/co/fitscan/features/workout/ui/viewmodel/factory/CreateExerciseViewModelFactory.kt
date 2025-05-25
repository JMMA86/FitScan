package icesi.edu.co.fitscan.features.workout.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import icesi.edu.co.fitscan.domain.repositories.IExerciseRepository
import icesi.edu.co.fitscan.domain.usecases.ICreateExerciseUseCase
import icesi.edu.co.fitscan.features.common.data.remote.RetrofitInstance
import icesi.edu.co.fitscan.features.workout.data.dataSources.IExerciseDataSource
import icesi.edu.co.fitscan.features.workout.data.mapper.ExerciseMapper
import icesi.edu.co.fitscan.features.workout.data.repositories.ExerciseRepositoryImpl
import icesi.edu.co.fitscan.features.workout.data.usecases.CreateExerciseUseCaseImpl
import icesi.edu.co.fitscan.features.workout.ui.viewmodel.CreateExerciseViewModel

class CreateExerciseViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateExerciseViewModel::class.java)) {
            val exerciseDataSource = RetrofitInstance.create(IExerciseDataSource::class.java)
            val exerciseMapper = ExerciseMapper()
            val exerciseRepository: IExerciseRepository = ExerciseRepositoryImpl(
                datasource = exerciseDataSource,
                mapper = exerciseMapper
            )
            val createExerciseUseCase: ICreateExerciseUseCase = CreateExerciseUseCaseImpl(exerciseRepository)
            @Suppress("UNCHECKED_CAST")
            return CreateExerciseViewModel(
                createExerciseUseCase = createExerciseUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
