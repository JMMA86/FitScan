package icesi.edu.co.fitscan.domain.usecases

interface IDeleteProgressPhotoUseCase {
    suspend operator fun invoke(photoId: String): Boolean
}
