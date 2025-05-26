package icesi.edu.co.fitscan.features.statistics.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import icesi.edu.co.fitscan.features.common.data.remote.RetrofitInstance
import icesi.edu.co.fitscan.features.statistics.data.remote.StatisticsRemoteDataSource
import icesi.edu.co.fitscan.features.statistics.data.repositories.StatisticsRepositoryImpl
import icesi.edu.co.fitscan.features.statistics.data.usecase.FetchWeeklyStatsUseCaseImpl
import icesi.edu.co.fitscan.features.statistics.data.usecase.FetchWeightStatsUseCaseImpl
import icesi.edu.co.fitscan.features.statistics.data.usecase.FetchCaloriesStatsUseCaseImpl
import icesi.edu.co.fitscan.features.statistics.ui.viewmodel.StatisticsViewModel

class StatisticsViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatisticsViewModel::class.java)) {
            val statisticsDataSource = RetrofitInstance.create(StatisticsRemoteDataSource::class.java)
            val repository = StatisticsRepositoryImpl(statisticsDataSource)
            val fetchWeeklyStatsUseCase = FetchWeeklyStatsUseCaseImpl(repository)
            val fetchWeightStatsUseCase = FetchWeightStatsUseCaseImpl(repository)
            val fetchCaloriesStatsUseCase = FetchCaloriesStatsUseCaseImpl(repository)
            @Suppress("UNCHECKED_CAST")
            return StatisticsViewModel(
                fetchWeeklyStatsUseCase,
                fetchWeightStatsUseCase,
                fetchCaloriesStatsUseCase,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
