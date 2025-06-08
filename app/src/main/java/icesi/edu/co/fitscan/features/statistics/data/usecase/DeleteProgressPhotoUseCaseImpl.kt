package icesi.edu.co.fitscan.features.statistics.data.usecase

import icesi.edu.co.fitscan.domain.repositories.IProgressPhotoRepository
import icesi.edu.co.fitscan.domain.usecases.IDeleteProgressPhotoUseCase

class DeleteProgressPhotoUseCaseImpl(
    private val repository: IProgressPhotoRepository
) : IDeleteProgressPhotoUseCase {
    override suspend operator fun invoke(photoId: String): Boolean {
        return repository.deleteProgressPhoto(photoId)
    }
}
