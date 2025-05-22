package icesi.edu.co.fitscan.features.statistics.ui.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import icesi.edu.co.fitscan.features.statistics.domain.service.ExerciseStatisticsService
import icesi.edu.co.fitscan.features.statistics.data.remote.ProgressPhoto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VisualProgressViewModel(
    private val service: ExerciseStatisticsService = ExerciseStatisticsService()
) : ViewModel() {
    private val _imageURL = MutableStateFlow("")
    val imageURL: StateFlow<String> = _imageURL.asStateFlow()

    private val _progressPhotos = MutableStateFlow<List<ProgressPhoto>>(emptyList())
    val progressPhotos: StateFlow<List<ProgressPhoto>> = _progressPhotos.asStateFlow()

    private val _isUploading = MutableStateFlow(false)
    val isUploading: StateFlow<Boolean> = _isUploading.asStateFlow()

    fun setImageURL(toString: String) {
        _imageURL.value = toString
    }

    init {
        loadProgressPhotos()
    }
    
    fun loadProgressPhotos() {
        viewModelScope.launch {
            _progressPhotos.value = service.fetchProgressPhotos()
            Log.i(">>>", "Progress photos loaded: ${_progressPhotos.value}")
        }
    }

    fun uploadProgressPhoto(context: Context, uri: Uri, title: String? = null) {
        viewModelScope.launch {
            _isUploading.value = true
            val success = service.uploadProgressPhoto(context, uri, title)
            if (success) {
                // After upload, make the file public
                service.makeLastUploadedFilePublic()
                loadProgressPhotos()
            }
            _isUploading.value = false
        }
    }
}