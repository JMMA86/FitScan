package icesi.edu.co.fitscan.features.workout.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import icesi.edu.co.fitscan.ui.theme.greenLess
import icesi.edu.co.fitscan.ui.theme.greyStrong

@Composable
fun CreateWorkoutButton(
    onClick: () -> Unit,
    icon: Int,
    text: String = "Crear entrenamiento",
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    Button(
        onClick = { if (!isLoading && enabled) onClick() },
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = greenLess,
            contentColor = Color.White,
            disabledContainerColor = greyStrong,
            disabledContentColor = Color.Gray
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = Color.White,
                strokeWidth = 2.dp
            )
        } else {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text, fontSize = 16.sp)
        }
    }
}
