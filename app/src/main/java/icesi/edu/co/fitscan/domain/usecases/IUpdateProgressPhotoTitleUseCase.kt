package icesi.edu.co.fitscan.domain.usecases

interface IUpdateProgressPhotoTitleUseCase {
    suspend operator fun invoke(photoId: String, title: String): Boolean
}
