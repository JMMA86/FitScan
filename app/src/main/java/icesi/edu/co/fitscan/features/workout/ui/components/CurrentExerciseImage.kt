package icesi.edu.co.fitscan.features.workout.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest
import icesi.edu.co.fitscan.features.workout.ui.util.ExerciseImageProvider

@Composable
fun CurrentExerciseImage(
    exerciseName: String,
    muscleGroups: String? = null,
    modifier: Modifier = Modifier,
    onDetailsClick: () -> Unit = {}
) {
    var imageState by remember { mutableStateOf<AsyncImagePainter.State?>(null) }
    var apiImageUrl by remember { mutableStateOf<String?>(null) }

    // Intentar obtener imagen de la API real
    LaunchedEffect(exerciseName, muscleGroups) {
        try {
            val url = ExerciseImageProvider.getSmartExerciseImageUrl(exerciseName, muscleGroups, 300, 120)
            apiImageUrl = url
        } catch (e: Exception) {
            apiImageUrl = null
        }
    }

    val fallbackUrl = remember(muscleGroups) {
        if (!muscleGroups.isNullOrBlank()) {
            ExerciseImageProvider.getImageByMuscleGroup(muscleGroups)
        } else {
            ExerciseImageProvider.getDefaultExerciseImageUrl(300, 120)
        }
    }

    // URL final a usar
    val finalImageUrl = apiImageUrl ?: fallbackUrl

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        when (imageState) {
            null, is AsyncImagePainter.State.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
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
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                }
            }

            is AsyncImagePainter.State.Error -> {
                if (finalImageUrl != fallbackUrl) {
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
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                                        MaterialTheme.colorScheme.tertiary.copy(alpha = 0.7f)
                                    )
                                )
                            )
                    )
                }
            }

            else -> {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(finalImageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Imagen del ejercicio $exerciseName",
                    contentScale = ContentScale.Crop,
                    onState = { imageState = it },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // Overlay con información del ejercicio
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            androidx.compose.ui.graphics.Color.Transparent,
                            androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.6f)
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        ) {
            // Nombre del ejercicio en la parte inferior
            Text(
                text = exerciseName,
                color = androidx.compose.ui.graphics.Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            )

            // Botón de detalles en la esquina superior derecha
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(32.dp)
                    .background(
                        androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.5f),
                        RoundedCornerShape(16.dp)
                    )
                    .clickable { onDetailsClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Visibility,
                    contentDescription = "Ver detalles",
                    tint = androidx.compose.ui.graphics.Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
