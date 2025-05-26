package icesi.edu.co.fitscan.features.statistics.data.usecase

import android.content.Context
import android.net.Uri
import icesi.edu.co.fitscan.domain.repositories.IProgressPhotoRepository
import icesi.edu.co.fitscan.domain.repositories.IStatisticsRepository
import icesi.edu.co.fitscan.domain.usecases.IUploadProgressPhotoUseCase

class UploadProgressPhotoUseCaseImpl(private val repository: IProgressPhotoRepository): IUploadProgressPhotoUseCase {
    override suspend operator fun invoke(context: Context, uri: Uri, title: String?): Boolean {
        return repository.uploadProgressPhoto(context, uri, title)
    }
}
