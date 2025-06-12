package icesi.edu.co.fitscan.features.workout.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
fun ExerciseImageThumbnail(
    exerciseName: String,
    muscleGroups: String? = null,
    modifier: Modifier = Modifier,
    size: Int = 60,
    isCircular: Boolean = true
) {
    var imageState by remember { mutableStateOf<AsyncImagePainter.State?>(null) }
    
    val imageUrl = remember(exerciseName, muscleGroups) {
        ExerciseImageProvider.getTranslatedExerciseImageUrl(exerciseName, size, size)
    }
    
    val fallbackUrl = remember(muscleGroups) {
        if (!muscleGroups.isNullOrBlank()) {
            ExerciseImageProvider.getImageByMuscleGroup(muscleGroups)
        } else {
            ExerciseImageProvider.getDefaultExerciseImageUrl(size, size)
        }
    }
    
    val shape = if (isCircular) CircleShape else RoundedCornerShape(8.dp)
    
    Box(
        modifier = modifier
            .size(size.dp)
            .clip(shape)
    ) {
        when (imageState) {
            null, is AsyncImagePainter.State.Loading -> {
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
            is AsyncImagePainter.State.Error -> {
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
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                        MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f)
                                    )
                                )
                            )
                    )
                }
            }
            else -> {
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
