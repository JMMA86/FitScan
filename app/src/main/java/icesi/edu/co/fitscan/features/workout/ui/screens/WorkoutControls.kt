package icesi.edu.co.fitscan.features.workout.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Dimensions.MediumCornerRadius))
            .padding(horizontal = Dimensions.MediumPadding),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = onPrevious,
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .size(Dimensions.LargeIconSize)
                .clip(RoundedCornerShape(50)),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Anterior",
                modifier = Modifier.size(Dimensions.MediumIconSize),
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Button(
            onClick = onPause,
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .size(Dimensions.LargeIconSize)
                .clip(RoundedCornerShape(50)),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.pause_outline_filled),
                contentDescription = "Detener workout",
                modifier = Modifier.size(Dimensions.MediumIconSize)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Button(
            onClick = onNext,
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .size(Dimensions.LargeIconSize)
                .clip(RoundedCornerShape(50)),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
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
