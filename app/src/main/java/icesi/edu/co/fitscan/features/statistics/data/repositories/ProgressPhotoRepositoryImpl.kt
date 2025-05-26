package icesi.edu.co.fitscan.features.statistics.data.repositories

import icesi.edu.co.fitscan.domain.model.ProgressPhoto
import icesi.edu.co.fitscan.domain.repositories.IProgressPhotoRepository
import icesi.edu.co.fitscan.features.common.ui.viewmodel.AppState
import icesi.edu.co.fitscan.features.statistics.data.dto.FileUpdateRequest
import icesi.edu.co.fitscan.features.statistics.data.dto.ProgressPhotoCreateRequest
import icesi.edu.co.fitscan.features.statistics.data.mapper.ProgressPhotoMapper
import icesi.edu.co.fitscan.features.statistics.data.remote.StatisticsRemoteDataSource

class ProgressPhotoRepositoryImpl(
    private val remoteDataSource: StatisticsRemoteDataSource
) : IProgressPhotoRepository {

    override suspend fun fetchProgressPhotos(): Result<List<ProgressPhoto>> {
        return try {
            val actualCustomerId = AppState.customerId ?: ""
            val response = remoteDataSource.getProgressPhotos(actualCustomerId)
            val photos = response.data.map { ProgressPhotoMapper.toDomain(it) }
            Result.success(photos)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun uploadProgressPhoto(context: android.content.Context, uri: android.net.Uri, title: String?): Boolean {
        return try {
            val token = "Bearer ${AppState.token ?: ""}" // Use Bearer token
            val customerId = AppState.customerId ?: return false
            // Prepare file part
            val filePart = icesi.edu.co.fitscan.features.common.data.local.MultipartProvider.get().prepareMultipartFromUri(uri)
            // Upload file
            val fileResponse = remoteDataSource.uploadFile(token, filePart)
            val fileId = fileResponse.data.id
            val fileName = fileResponse.data.filename_download
            // Create progress photo record
            val now = java.time.LocalDate.now().toString()
            val createRequest = ProgressPhotoCreateRequest(
                customer_id = customerId,
                photo_date = now,
                title = title,
                image = fileName,
                image_path = fileId
            )
            val createResponse = remoteDataSource.createProgressPhoto(createRequest)
            if (!createResponse.isSuccessful) return false
            // Make file public
            val makePublicResponse = remoteDataSource.makeFilePublic(fileId, FileUpdateRequest(true))
            makePublicResponse.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}
