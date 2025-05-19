package icesi.edu.co.fitscan.features.workout.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun FitScanSaveButton(
    onClick: () -> Unit,
    iconResId: Int,
    isLoading: Boolean = false
) {
    Button(
        onClick = { if (!isLoading) onClick() },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = Color.White,
                strokeWidth = 2.dp
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = iconResId),
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Guardar entrenamiento", color = Color.White)
            }
        }
    }
}