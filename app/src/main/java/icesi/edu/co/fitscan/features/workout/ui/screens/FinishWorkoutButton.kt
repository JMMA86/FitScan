package icesi.edu.co.fitscan.features.workout.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import icesi.edu.co.fitscan.ui.theme.Dimensions

@Composable
fun FinishWorkoutButton(onFinish: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = onFinish,
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.error
            ),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.error)
        ) {
            Text(
                text = "Terminar entrenamiento",
                fontSize = Dimensions.MediumTextSize,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

