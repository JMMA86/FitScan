package icesi.edu.co.fitscan.features.home.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import icesi.edu.co.fitscan.ui.theme.dashboardGreen
import icesi.edu.co.fitscan.ui.theme.greySuperLight

@Composable
fun CircularProgress(progress: Float) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(140.dp)) {
        CircularProgressIndicator(
            progress = { progress },
            strokeWidth = 10.dp,
            modifier = Modifier.fillMaxSize(),
            color = dashboardGreen,
            trackColor = Color.DarkGray
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )
            Text(
                text = "Completado",
                style = MaterialTheme.typography.bodySmall,
                color = greySuperLight
            )
        }
    }
}