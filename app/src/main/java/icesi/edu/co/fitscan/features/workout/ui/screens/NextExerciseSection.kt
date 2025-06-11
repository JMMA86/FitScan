package icesi.edu.co.fitscan.features.workout.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import icesi.edu.co.fitscan.ui.theme.Dimensions

@Composable
fun NextExerciseSection(
    name: String,
    sets: String,
    reps: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimensions.MediumPadding) // Separate from screen borders
            .clip(RoundedCornerShape(Dimensions.MediumCornerRadius))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Text(
            text = "Siguiente ejercicio",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = name,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = Dimensions.LargeTextSize,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
        )
        Row {
            Text(
                text = "$sets sets | $reps reps",
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}
