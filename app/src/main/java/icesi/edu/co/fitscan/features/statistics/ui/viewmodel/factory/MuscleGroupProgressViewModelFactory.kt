package icesi.edu.co.fitscan.features.statistics.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import icesi.edu.co.fitscan.features.common.data.remote.RetrofitInstance
import icesi.edu.co.fitscan.features.statistics.data.remote.StatisticsRemoteDataSource
import icesi.edu.co.fitscan.features.statistics.data.repositories.MuscleGroupRepositoryImpl
import icesi.edu.co.fitscan.features.statistics.data.usecase.FetchAllMuscleGroupsUseCaseImpl
import icesi.edu.co.fitscan.features.statistics.data.usecase.FetchMuscleGroupProgressUseCaseImpl
import icesi.edu.co.fitscan.features.statistics.ui.viewmodel.MuscleGroupProgressViewModel

class MuscleGroupProgressViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MuscleGroupProgressViewModel::class.java)) {
            val statisticsDataSource = RetrofitInstance.create(StatisticsRemoteDataSource::class.java)
            val repository = MuscleGroupRepositoryImpl(statisticsDataSource)
            val fetchAllMuscleGroupsUseCase = FetchAllMuscleGroupsUseCaseImpl(repository)
            val fetchMuscleGroupProgressUseCase = FetchMuscleGroupProgressUseCaseImpl(repository)
            
            @Suppress("UNCHECKED_CAST")
            return MuscleGroupProgressViewModel(
                fetchAllMuscleGroupsUseCase,
                fetchMuscleGroupProgressUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
