package icesi.edu.co.fitscan.features.statistics.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import icesi.edu.co.fitscan.features.common.data.remote.RetrofitInstance
import icesi.edu.co.fitscan.features.statistics.data.remote.StatisticsRemoteDataSource
import icesi.edu.co.fitscan.features.statistics.data.repositories.ExerciseProgressRepositoryImpl
import icesi.edu.co.fitscan.features.statistics.data.usecase.FetchAllExercisesUseCaseImpl
import icesi.edu.co.fitscan.features.statistics.data.usecase.FetchExerciseProgressUseCaseImpl
import icesi.edu.co.fitscan.features.statistics.ui.viewmodel.ExerciseProgressViewModel

class ExerciseProgressViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExerciseProgressViewModel::class.java)) {
            val statisticsDataSource = RetrofitInstance.create(StatisticsRemoteDataSource::class.java)
            val repository = ExerciseProgressRepositoryImpl(statisticsDataSource)
            val fetchExerciseProgressUseCase = FetchExerciseProgressUseCaseImpl(repository)
            val fetchAllExercisesUseCase = FetchAllExercisesUseCaseImpl(repository)
            @Suppress("UNCHECKED_CAST")
            return ExerciseProgressViewModel(fetchExerciseProgressUseCase, fetchAllExercisesUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
