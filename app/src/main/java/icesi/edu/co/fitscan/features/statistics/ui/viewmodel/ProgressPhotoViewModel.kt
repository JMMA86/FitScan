package icesi.edu.co.fitscan.features.statistics.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import icesi.edu.co.fitscan.domain.model.ProgressPhoto
import icesi.edu.co.fitscan.domain.usecases.IFetchProgressPhotosUseCase
import icesi.edu.co.fitscan.domain.usecases.IUpdateProgressPhotoTitleUseCase
import icesi.edu.co.fitscan.domain.usecases.IUploadProgressPhotoUseCase
import icesi.edu.co.fitscan.features.common.ui.viewmodel.AppState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.asStateFlow

class ProgressPhotoViewModel(
    private val fetchProgressPhotosUseCase: IFetchProgressPhotosUseCase,
    private val uploadProgressPhotoUseCase: IUploadProgressPhotoUseCase,
    private val updateProgressPhotoTitleUseCase: IUpdateProgressPhotoTitleUseCase
) : ViewModel() {

    private val _progressPhotos = MutableStateFlow<List<ProgressPhoto>>(emptyList())
    val progressPhotos: StateFlow<List<ProgressPhoto>> = _progressPhotos.asStateFlow()

    private val _isUploading = MutableStateFlow(false)
    val isUploading: StateFlow<Boolean> = _isUploading.asStateFlow()

    init {
        loadProgressPhotos()
    }

    private fun loadProgressPhotos() {
        val customerId = AppState.customerId ?: return
        viewModelScope.launch {
            val result = fetchProgressPhotosUseCase(customerId)
            result.onSuccess { _progressPhotos.value = it }
        }
    }
    
    fun uploadProgressPhoto(context: Context, uri: Uri, title: String? = null) {
        viewModelScope.launch {
            _isUploading.value = true
            uploadProgressPhotoUseCase(context, uri, title)
            loadProgressPhotos()
            _isUploading.value = false
        }
    }

    fun updateProgressPhotoTitle(photoId: String, title: String) {
        viewModelScope.launch {
            val success = updateProgressPhotoTitleUseCase(photoId, title)
            if (success) {
                loadProgressPhotos()
            }
        }
    }
}
