package icesi.edu.co.fitscan.features.workout.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun WorkoutThumbnail(
    workoutName: String,
    workoutType: String? = null,
    modifier: Modifier = Modifier,
    size: Int = 60,
    shape: String = "rounded" // "rounded" or "circular"
) {
    var imageState by remember { mutableStateOf<AsyncImagePainter.State?>(null) }
    
    // Generate workout image URL based on workout name and type
    val imageUrl = remember(workoutName, workoutType) {
        // Use workout type or general fitness terms for image search
        val searchTerm = when {
            workoutType?.contains("gym", ignoreCase = true) == true -> "gym workout"
            workoutType?.contains("cardio", ignoreCase = true) == true -> "cardio fitness"
            workoutType?.contains("strength", ignoreCase = true) == true -> "strength training"
            workoutName.contains("fuerza", ignoreCase = true) -> "strength training"
            workoutName.contains("cardio", ignoreCase = true) -> "cardio workout"
            workoutName.contains("yoga", ignoreCase = true) -> "yoga meditation"
            workoutName.contains("running", ignoreCase = true) || workoutName.contains("correr", ignoreCase = true) -> "running fitness"
            else -> "fitness workout"
        }
        "https://source.unsplash.com/${size}x${size}/?$searchTerm"
    }
    
    val containerShape = if (shape == "circular") {
        RoundedCornerShape(50)
    } else {
        RoundedCornerShape(12.dp)
    }
    
    Box(
        modifier = modifier
            .size(size.dp)
            .clip(containerShape)
    ) {
        when (imageState) {
            null, is AsyncImagePainter.State.Loading -> {
                // Show gradient while loading
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f)
                                )
                            )
                        )
                )
            }
            is AsyncImagePainter.State.Error -> {
                // Fallback to gradient on error
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                    MaterialTheme.colorScheme.tertiary.copy(alpha = 0.8f)
                                )
                            )
                        )
                )
            }
            else -> {
                // Image loaded successfully
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Imagen del entrenamiento $workoutName",
                    contentScale = ContentScale.Crop,
                    onState = { imageState = it },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
