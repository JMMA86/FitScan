package icesi.edu.co.fitscan.domain.usecases

import android.content.Context
import android.net.Uri

interface IUploadProgressPhotoUseCase {
    suspend operator fun invoke(context: Context, uri: Uri, title: String? = null): Boolean
}
