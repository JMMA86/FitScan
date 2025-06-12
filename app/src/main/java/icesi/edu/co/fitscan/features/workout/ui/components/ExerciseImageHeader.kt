package icesi.edu.co.fitscan.features.workout.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
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
import icesi.edu.co.fitscan.features.workout.ui.util.ExerciseVisual
import kotlinx.coroutines.launch

@Composable
fun ExerciseImageHeader(
    exerciseName: String,
    muscleGroups: String? = null,
    modifier: Modifier = Modifier,
    height: Int = 180
) {
    var imageState by remember { mutableStateOf<AsyncImagePainter.State?>(null) }
    var apiImageUrl by remember { mutableStateOf<String?>(null) }

    // Intentar obtener imagen de la API real
    LaunchedEffect(exerciseName, muscleGroups) {
        Log.d(
            "ExerciseImageHeader",
            "🚀 Starting image search for '$exerciseName' with muscles: $muscleGroups"
        )
        try {
            val url = ExerciseImageProvider.getSmartExerciseImageUrl(
                exerciseName,
                muscleGroups,
                400,
                height
            )
            apiImageUrl = url
            Log.i("ExerciseImageHeader", "✅ Got smart URL for '$exerciseName': $url")
        } catch (e: Exception) {
            Log.e(
                "ExerciseImageHeader",
                "🚨 Failed to get smart URL for '$exerciseName': ${e.message}",
                e
            )
            apiImageUrl = null
        }
    }

    val fallbackUrl = remember(muscleGroups) {
        // Si falla, usar el grupo muscular
        if (!muscleGroups.isNullOrBlank()) {
            ExerciseImageProvider.getImageByMuscleGroup(muscleGroups)
        } else {
            ExerciseImageProvider.getDefaultExerciseImageUrl(400, height)
        }
    }

    // URL final a usar
    val finalImageUrl = apiImageUrl ?: fallbackUrl

    // Disparar recomposición cuando la URL final esté disponible
    LaunchedEffect(finalImageUrl) {
        Log.d("ExerciseImageHeader", "🎯 Final URL ready for '$exerciseName': $finalImageUrl")
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height.dp)
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
    ) {
        // Si no tenemos URL de la API aún, mostrar carga
        if (apiImageUrl == null) {
            // Mostrar gradiente mientras carga la API
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.tertiary
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(32.dp)
                )
            }
        } else {
            // Tenemos una URL, mostrar la imagen
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(finalImageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Imagen del ejercicio $exerciseName",
                contentScale = ContentScale.Crop,
                onState = { newState ->
                    Log.d(
                        "ExerciseImageHeader",
                        "📡 Image state for '$exerciseName': ${newState::class.simpleName} - URL: $finalImageUrl"
                    )
                    when (newState) {
                        is AsyncImagePainter.State.Success -> {
                            Log.i(
                                "ExerciseImageHeader",
                                "✅ Image loaded successfully for '$exerciseName'"
                            )
                        }

                        is AsyncImagePainter.State.Error -> {
                            Log.e(
                                "ExerciseImageHeader",
                                "❌ Image failed for '$exerciseName': ${newState.result.throwable?.message}"
                            )
                        }

                        is AsyncImagePainter.State.Loading -> {
                            Log.d("ExerciseImageHeader", "⏳ Image loading for '$exerciseName'...")
                        }

                        else -> {}
                    }
                    imageState = newState
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

/**
 * Header mejorado que combina imágenes e iconos
 */
@Composable
fun ExerciseImageHeaderV2(
    exerciseName: String,
    muscleGroups: String? = null,
    modifier: Modifier = Modifier,
    height: androidx.compose.ui.unit.Dp = 200.dp
) {
    ExerciseVisualHeader(
        exerciseName = exerciseName,
        muscleGroups = muscleGroups,
        modifier = modifier,
        height = height
    )
}

/**
 * Versión original mantenida para compatibilidad
 */