package icesi.edu.co.fitscan.features.workout.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest
import icesi.edu.co.fitscan.features.workout.ui.util.ExerciseImageProvider

@Composable
fun ExerciseImageThumbnail(
    exerciseName: String,
    muscleGroups: String? = null,
    modifier: Modifier = Modifier,
    size: Int = 60,
    isCircular: Boolean = true
) {
    var imageState by remember { mutableStateOf<AsyncImagePainter.State?>(null) }
    var apiImageUrl by remember { mutableStateOf<String?>(null) }

    // Intentar obtener imagen de la API real
    LaunchedEffect(exerciseName, muscleGroups) {
        Log.d("ExerciseImageThumbnail", "🚀 Starting API request for '$exerciseName'...")
        try {
            val url = ExerciseImageProvider.getExerciseImageUrl(exerciseName, muscleGroups)
            apiImageUrl = url
            Log.d("ExerciseImageThumbnail", "🌐 API returned URL for '$exerciseName': $url")
            if (url != null) {
                Log.i("ExerciseImageThumbnail", "✅ Setting API URL for '$exerciseName': $url")
            } else {
                Log.w("ExerciseImageThumbnail", "⚠️ API returned null for '$exerciseName'")
            }
        } catch (e: Exception) {
            Log.e("ExerciseImageThumbnail", "🚨 Failed to get API image for '$exerciseName': ${e.message}")
            apiImageUrl = null
        }
    }

    // URL de fallback usando placeholder
    val fallbackUrl = remember(exerciseName, muscleGroups) {
        val url = ExerciseImageProvider.getTranslatedExerciseImageUrl(exerciseName, size, size)
        Log.d("ExerciseImageThumbnail", "🔄 Generated fallback URL for '$exerciseName': $url")
        url
    }

    // URL final a usar
    val finalImageUrl = apiImageUrl ?: fallbackUrl
    
    Log.d("ExerciseImageThumbnail", "🎯 Final URL for '$exerciseName': $finalImageUrl (API: $apiImageUrl, Fallback: $fallbackUrl)")

    val shape = if (isCircular) CircleShape else RoundedCornerShape(8.dp)

    Box(
        modifier = modifier
            .size(size.dp)
            .clip(shape)
    ) {
        // Si tenemos una URL (API o fallback), mostrar la imagen
        if (finalImageUrl.isNotEmpty()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(finalImageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Imagen del ejercicio $exerciseName",
                contentScale = ContentScale.Crop,
                onState = { newState ->
                    Log.d(
                        "ExerciseImageThumbnail",
                        "📡 Image state for '$exerciseName': ${newState::class.simpleName} - URL: $finalImageUrl"
                    )
                    when (newState) {
                        is AsyncImagePainter.State.Success -> {
                            Log.i(
                                "ExerciseImageThumbnail",
                                "✅ Image loaded successfully for '$exerciseName' from: $finalImageUrl"
                            )
                        }

                        is AsyncImagePainter.State.Error -> {
                            Log.e(
                                "ExerciseImageThumbnail",
                                "❌ Image failed for '$exerciseName' from URL: $finalImageUrl. Error: ${newState.result.throwable?.message}"
                            )
                        }

                        is AsyncImagePainter.State.Loading -> {
                            Log.d(
                                "ExerciseImageThumbnail",
                                "⏳ Image loading for '$exerciseName' from: $finalImageUrl"
                            )
                        }

                        AsyncImagePainter.State.Empty -> {
                            Log.d("ExerciseImageThumbnail", "📭 Empty state for '$exerciseName'")
                        }
                    }
                    imageState = newState
                },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // Mostrar loading mientras esperamos la URL
            Log.d("ExerciseImageThumbnail", "⏳ No URL yet for '$exerciseName', showing loading...")
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp
                )
            }
        }
    }
}
