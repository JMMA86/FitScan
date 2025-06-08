package icesi.edu.co.fitscan.features.statistics.data.usecase

import icesi.edu.co.fitscan.domain.repositories.IProgressPhotoRepository
import icesi.edu.co.fitscan.domain.usecases.IUpdateProgressPhotoTitleUseCase

class UpdateProgressPhotoTitleUseCaseImpl(
    private val repository: IProgressPhotoRepository
) : IUpdateProgressPhotoTitleUseCase {
    override suspend operator fun invoke(photoId: String, title: String): Boolean {
        return repository.updateProgressPhotoTitle(photoId, title)
    }
}
