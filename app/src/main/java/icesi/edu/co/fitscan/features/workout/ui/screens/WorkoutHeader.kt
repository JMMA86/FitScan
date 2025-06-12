package icesi.edu.co.fitscan.features.workout.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import icesi.edu.co.fitscan.ui.theme.Dimensions

@Composable
fun WorkoutHeader(title: String, subtitle: String, progress: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimensions.MediumPadding)
    ) {
        Text(
            text = title,
            fontSize = Dimensions.XLargeTextSize,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(Dimensions.SmallPadding))
        Text(
            text = subtitle,
            fontSize = Dimensions.SmallTextSize,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(Dimensions.SmallPadding))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(Dimensions.MediumCornerRadius))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(Dimensions.SmallPadding)
        ) {
            Text(
                text = progress,
                fontSize = Dimensions.MediumTextSize,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

