package icesi.edu.co.fitscan.domain.repositories

import android.content.Context
import android.net.Uri
import icesi.edu.co.fitscan.domain.model.ProgressPhoto

interface IProgressPhotoRepository {
    suspend fun fetchProgressPhotos(): Result<List<ProgressPhoto>>
    suspend fun uploadProgressPhoto(context: Context, uri: Uri, title: String?): Boolean
    suspend fun updateProgressPhotoTitle(photoId: String, title: String): Boolean
}
