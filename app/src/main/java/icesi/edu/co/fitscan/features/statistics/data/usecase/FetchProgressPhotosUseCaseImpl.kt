package icesi.edu.co.fitscan.features.statistics.data.usecase

import icesi.edu.co.fitscan.domain.model.ProgressPhoto
import icesi.edu.co.fitscan.domain.repositories.IProgressPhotoRepository
import icesi.edu.co.fitscan.domain.repositories.IStatisticsRepository
import icesi.edu.co.fitscan.domain.usecases.IFetchProgressPhotosUseCase

class FetchProgressPhotosUseCaseImpl(private val repository: IProgressPhotoRepository): IFetchProgressPhotosUseCase {
    override suspend operator fun invoke(customerId: String): Result<List<ProgressPhoto>> {
        return repository.fetchProgressPhotos()
    }
}
