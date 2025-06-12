package icesi.edu.co.fitscan.features.workout.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import icesi.edu.co.fitscan.features.workout.ui.util.ExerciseImageProvider
import icesi.edu.co.fitscan.features.workout.ui.util.ExerciseVisual
import kotlinx.coroutines.launch

/**
 * Componente que muestra la representación visual de un ejercicio
 * Puede ser una imagen (URL) o un icono, con transiciones suaves
 */
@Composable
fun ExerciseVisualComponent(
    exerciseName: String,
    muscleGroups: String? = null,
    modifier: Modifier = Modifier,
    iconSize: androidx.compose.ui.unit.Dp = 48.dp,
    iconTint: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentDescription: String? = null,
    cornerRadius: androidx.compose.ui.unit.Dp = 12.dp,
    showLoading: Boolean = true
) {
    var visual by remember { mutableStateOf<ExerciseVisual?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    
    // Obtener representación visual del ejercicio
    LaunchedEffect(exerciseName, muscleGroups) {
        scope.launch {
            isLoading = true
            
            // Primero intentar la versión síncrona para respuesta inmediata
            val syncVisual = ExerciseImageProvider.getExerciseVisualSync(exerciseName, muscleGroups)
            visual = syncVisual
            
            // Si es un icono, intentar obtener imagen en segundo plano
            if (syncVisual is ExerciseVisual.Icon) {
                val asyncVisual = ExerciseImageProvider.getExerciseVisual(exerciseName, muscleGroups)
                if (asyncVisual is ExerciseVisual.ImageUrl) {
                    visual = asyncVisual
                }
            }
            
            isLoading = false
        }
    }
    
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        when (val currentVisual = visual) {
            is ExerciseVisual.ImageUrl -> {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(currentVisual.url)
                        .crossfade(true)
                        .build(),
                    contentDescription = contentDescription ?: "Imagen de $exerciseName",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            
            is ExerciseVisual.Icon -> {
                Icon(
                    imageVector = currentVisual.icon,
                    contentDescription = contentDescription ?: "Icono de $exerciseName",
                    modifier = Modifier.size(iconSize),
                    tint = iconTint
                )
            }
            
            null -> {
                if (showLoading && isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(iconSize / 2),
                        color = iconTint,
                        strokeWidth = 2.dp
                    )
                }
            }
        }
    }
}

/**
 * Versión thumbnail del componente visual
 */
@Composable
fun ExerciseVisualThumbnail(
    exerciseName: String,
    muscleGroups: String? = null,
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 60.dp
) {
    ExerciseVisualComponent(
        exerciseName = exerciseName,
        muscleGroups = muscleGroups,
        modifier = modifier.size(size),
        iconSize = size * 0.6f,
        cornerRadius = 8.dp
    )
}

/**
 * Versión header del componente visual
 */
@Composable
fun ExerciseVisualHeader(
    exerciseName: String,
    muscleGroups: String? = null,
    modifier: Modifier = Modifier,
    height: androidx.compose.ui.unit.Dp = 200.dp
) {
    ExerciseVisualComponent(
        exerciseName = exerciseName,
        muscleGroups = muscleGroups,
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        iconSize = 80.dp,
        cornerRadius = 16.dp
    )
}

/**
 * Versión compacta para listas pequeñas
 */
@Composable
fun ExerciseVisualCompact(
    exerciseName: String,
    muscleGroups: String? = null,
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 40.dp
) {
    ExerciseVisualComponent(
        exerciseName = exerciseName,
        muscleGroups = muscleGroups,
        modifier = modifier.size(size),
        iconSize = size * 0.7f,
        cornerRadius = 6.dp,
        showLoading = false
    )
}
