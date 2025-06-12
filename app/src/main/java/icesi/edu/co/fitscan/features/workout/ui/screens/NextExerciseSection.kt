package icesi.edu.co.fitscan.features.workout.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import icesi.edu.co.fitscan.features.workout.ui.components.ExerciseImageThumbnail
import icesi.edu.co.fitscan.ui.theme.Dimensions

@Composable
fun NextExerciseSection(
    name: String,
    sets: String,
    reps: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimensions.MediumPadding) // Separate from screen borders
            .clip(RoundedCornerShape(Dimensions.MediumCornerRadius))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onClick() }
            .padding(Dimensions.SmallPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Exercise thumbnail
        ExerciseImageThumbnail(
            exerciseName = name,
            size = 48,
            isCircular = true
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Siguiente ejercicio",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = Dimensions.SmallTextSize
            )
            Text(
                text = name,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = Dimensions.LargeTextSize,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
            Text(
                text = "$sets sets | $reps reps",
                color = MaterialTheme.colorScheme.primary,
                fontSize = Dimensions.MediumTextSize
            )
        }
    }
}
