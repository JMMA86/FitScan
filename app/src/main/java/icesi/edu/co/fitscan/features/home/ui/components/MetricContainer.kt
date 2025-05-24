package icesi.edu.co.fitscan.features.home.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MetricContainer(metrics: Triple<String, String, String>) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.DarkGray.copy(alpha = 0.8f),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            MetricCard(value = metrics.first, label = "Entrenamiento\nsemanal")
            MetricCard(value = metrics.second, label = "Días\nentrenados")
            MetricCard(value = metrics.third, label = "Distancia total")
        }
    }
}