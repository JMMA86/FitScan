package icesi.edu.co.fitscan.features.workout.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import icesi.edu.co.fitscan.R
import icesi.edu.co.fitscan.ui.theme.Dimensions

@Composable
fun WorkoutControls(
    onPrevious: () -> Unit,
    onPause: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = onPrevious,
            shape = RectangleShape,
            modifier = Modifier
                .size(Dimensions.LargeIconSize)
                .clip(RoundedCornerShape(Dimensions.MediumCornerRadius)),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Anterior",
                modifier = Modifier.size(Dimensions.MediumIconSize),
            )
        }
        Spacer(modifier = Modifier.width(6.dp))
        Button(
            onClick = onPause,
            shape = RectangleShape,
            modifier = Modifier
                .size(Dimensions.LargeIconSize)
                .clip(RoundedCornerShape(Dimensions.MediumCornerRadius)),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.pause_outline_filled),
                contentDescription = "Detener workout",
                modifier = Modifier.size(Dimensions.MediumIconSize)
            )
        }
        Spacer(modifier = Modifier.width(6.dp))
        Button(
            onClick = onNext,
            shape = RectangleShape,
            modifier = Modifier
                .size(Dimensions.LargeIconSize)
                .clip(RoundedCornerShape(Dimensions.MediumCornerRadius)),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Siguiente",
                modifier = Modifier
                    .size(Dimensions.MediumIconSize)
                    .graphicsLayer(scaleX = -1f)
            )
        }
    }
}

