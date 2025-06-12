package icesi.edu.co.fitscan.features.workout.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
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
fun ExerciseImageHeader(
    exerciseName: String,
    muscleGroups: String? = null,
    modifier: Modifier = Modifier,
    height: Int = 180
) {
    var imageState by remember { mutableStateOf<AsyncImagePainter.State?>(null) }
    
    val imageUrl = remember(exerciseName, muscleGroups) {
        // Usar la búsqueda inteligente que combina nombre y grupo muscular
        ExerciseImageProvider.getSmartExerciseImageUrl(exerciseName, muscleGroups, 400, height)
    }
    
    val fallbackUrl = remember(muscleGroups) {
        // Si falla, usar el grupo muscular
        if (!muscleGroups.isNullOrBlank()) {
            ExerciseImageProvider.getImageByMuscleGroup(muscleGroups)
        } else {
            ExerciseImageProvider.getDefaultExerciseImageUrl(400, height)
        }
    }
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height.dp)
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
    ) {
        when (imageState) {
            null, is AsyncImagePainter.State.Loading -> {
                // Mostrar gradiente mientras carga
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
            }
            is AsyncImagePainter.State.Error -> {
                // Si hay error, intentar con el fallback o mostrar gradiente
                if (imageUrl != fallbackUrl) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(fallbackUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Imagen del ejercicio $exerciseName",
                        contentScale = ContentScale.Crop,
                        onState = { imageState = it },
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    // Fallback final: gradiente
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
                            )
                    )
                }
            }
            else -> {
                // Imagen cargada exitosamente
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Imagen del ejercicio $exerciseName",
                    contentScale = ContentScale.Crop,
                    onState = { imageState = it },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}