package icesi.edu.co.fitscan.features.common.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import icesi.edu.co.fitscan.ui.theme.cardBackground

@Composable
fun GoalCard(
    title: String,
    areaData: List<Float>,
    color: Color,
    barColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.cardBackground,
        shape = RoundedCornerShape(18.dp),
        modifier = modifier.height(140.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title, 
                color = MaterialTheme.colorScheme.onSurface, 
                fontWeight = FontWeight.Bold, 
                fontSize = 18.sp
            )
            Text(
                text = "Progreso", 
                color = MaterialTheme.colorScheme.onSurfaceVariant, 
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            AreaChart(
                data = areaData,
                color = color,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .background(barColor, shape = RoundedCornerShape(8.dp))
            ) {}
        }
    }
}

@Composable
fun AreaChart(data: List<Float>, color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        if (data.isEmpty()) return@Canvas
        val maxVal = data.maxOrNull() ?: 1f
        val minVal = data.minOrNull() ?: 0f
        val points = data.mapIndexed { i, v ->
            Offset(
                x = i * size.width / (data.size - 1).coerceAtLeast(1),
                y = size.height - ((v - minVal) / (maxVal - minVal + 0.0001f)) * size.height
            )
        }
        val path = Path().apply {
            moveTo(points.first().x, size.height)
            points.forEach { lineTo(it.x, it.y) }
            lineTo(points.last().x, size.height)
            close()
        }
        drawPath(
            path = path,
            brush = Brush.verticalGradient(listOf(color.copy(alpha = 0.7f), color.copy(alpha = 0.2f), Color.Transparent)),
            style = Fill
        )
        // Draw the line on top
        val linePath = Path().apply {
            moveTo(points.first().x, points.first().y)
            points.forEach { lineTo(it.x, it.y) }
        }
        drawPath(
            path = linePath,
            color = color,
            style = Stroke(width = 3.dp.toPx())
        )
    }
}
