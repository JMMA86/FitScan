package icesi.edu.co.fitscan.features.statistics.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import icesi.edu.co.fitscan.features.common.data.remote.RetrofitInstance
import icesi.edu.co.fitscan.features.statistics.data.remote.StatisticsRemoteDataSource
import icesi.edu.co.fitscan.features.statistics.data.repositories.ProgressPhotoRepositoryImpl
import icesi.edu.co.fitscan.features.statistics.data.usecase.FetchProgressPhotosUseCaseImpl
import icesi.edu.co.fitscan.features.statistics.data.usecase.UploadProgressPhotoUseCaseImpl
import icesi.edu.co.fitscan.features.statistics.ui.viewmodel.ProgressPhotoViewModel

class ProgressPhotoViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProgressPhotoViewModel::class.java)) {
            val statisticsDataSource = RetrofitInstance.create(StatisticsRemoteDataSource::class.java)
            val repository = ProgressPhotoRepositoryImpl(statisticsDataSource)
            val fetchProgressPhotosUseCase = FetchProgressPhotosUseCaseImpl(repository)
            val uploadProgressPhotoUseCase = UploadProgressPhotoUseCaseImpl(repository)
            @Suppress("UNCHECKED_CAST")
            return ProgressPhotoViewModel(fetchProgressPhotosUseCase, uploadProgressPhotoUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
