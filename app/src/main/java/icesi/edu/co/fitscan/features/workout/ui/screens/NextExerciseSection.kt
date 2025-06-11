package icesi.edu.co.fitscan.features.workout.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import icesi.edu.co.fitscan.ui.theme.Dimensions

@Composable
fun NextExerciseSection(
    name: String,
    sets: String,
    reps: String
) {
    Column(
        modifier = Modifier.padding(Dimensions.MediumPadding)
    ) {
        Text(
            text = "Siguiente ejercicio",
            color = Color.White
        )
        Text(
            text = name,
            color = Color.White,
            fontSize = Dimensions.LargeTextSize,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
        )
        Row {
            Text(
                text = "$sets sets | $reps reps",
                color = Color.White,
            )
        }
    }
}

