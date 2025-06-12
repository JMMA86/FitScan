package icesi.edu.co.fitscan.features.workout.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import icesi.edu.co.fitscan.features.workout.ui.util.ExerciseIconProvider

@Composable
fun ExerciseIconHeader(
    exerciseName: String,
    muscleGroups: String? = null,
    modifier: Modifier = Modifier,
    height: Int = 180
) {
    val icon = ExerciseIconProvider.getBestIcon(exerciseName, muscleGroups)
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height.dp)
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
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
        Icon(
            imageVector = icon,
            contentDescription = "Icono del ejercicio $exerciseName",
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(64.dp)
        )
    }
}
