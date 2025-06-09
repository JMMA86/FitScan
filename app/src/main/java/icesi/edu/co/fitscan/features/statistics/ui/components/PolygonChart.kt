package icesi.edu.co.fitscan.features.statistics.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import icesi.edu.co.fitscan.domain.model.MuscleGroupProgress
import kotlin.math.*

@Composable
fun PolygonChart(
    muscleGroupProgress: List<MuscleGroupProgress>,
    modifier: Modifier = Modifier,
    maxValue: Float = 1.0f
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
    val surfaceColor = MaterialTheme.colorScheme.surface
    
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(16.dp)
    ) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val radius = (size.minDimension / 2f) * 0.8f
        
        if (muscleGroupProgress.isNotEmpty()) {
            // Draw background grid circles
            drawGrid(center, radius, surfaceColor)
            
            // Draw polygon based on muscle group progress
            drawProgressPolygon(
                muscleGroupProgress = muscleGroupProgress,
                center = center,
                radius = radius,
                maxValue = maxValue,
                fillColor = secondaryColor,
                strokeColor = primaryColor
            )
            
            // Draw axis lines and labels
            drawAxisLines(
                muscleGroupProgress = muscleGroupProgress,
                center = center,
                radius = radius,
                axisColor = surfaceColor
            )
        }
    }
}

private fun DrawScope.drawGrid(
    center: Offset,
    radius: Float,
    gridColor: androidx.compose.ui.graphics.Color
) {
    // Draw concentric circles for grid
    for (i in 1..4) {
        val circleRadius = radius * (i / 4f)
        drawCircle(
            color = gridColor,
            radius = circleRadius,
            center = center,
            style = Stroke(width = 1.dp.toPx())
        )
    }
}

private fun DrawScope.drawProgressPolygon(
    muscleGroupProgress: List<MuscleGroupProgress>,
    center: Offset,
    radius: Float,
    maxValue: Float,
    fillColor: androidx.compose.ui.graphics.Color,
    strokeColor: androidx.compose.ui.graphics.Color
) {
    val points = mutableListOf<Offset>()
    val angleStep = 2 * PI / muscleGroupProgress.size
    
    muscleGroupProgress.forEachIndexed { index, progress ->
        val angle = angleStep * index - PI / 2 // Start from top
        val progressRadius = radius * (progress.progressPercentage / maxValue).coerceIn(0f, 1f)
        
        val x = center.x + progressRadius * cos(angle).toFloat()
        val y = center.y + progressRadius * sin(angle).toFloat()
        points.add(Offset(x, y))
    }
    
    if (points.size >= 3) {
        // Create path for polygon
        val path = Path().apply {
            moveTo(points[0].x, points[0].y)
            for (i in 1 until points.size) {
                lineTo(points[i].x, points[i].y)
            }
            close()
        }
        
        // Draw filled polygon
        drawPath(
            path = path,
            color = fillColor
        )
        
        // Draw polygon outline
        drawPath(
            path = path,
            color = strokeColor,
            style = Stroke(width = 2.dp.toPx())
        )
        
        // Draw points
        points.forEach { point ->
            drawCircle(
                color = strokeColor,
                radius = 4.dp.toPx(),
                center = point
            )
        }
    }
}

private fun DrawScope.drawAxisLines(
    muscleGroupProgress: List<MuscleGroupProgress>,
    center: Offset,
    radius: Float,
    axisColor: androidx.compose.ui.graphics.Color
) {
    val angleStep = 2 * PI / muscleGroupProgress.size
    
    muscleGroupProgress.forEachIndexed { index, _ ->
        val angle = angleStep * index - PI / 2 // Start from top
        val endX = center.x + radius * cos(angle).toFloat()
        val endY = center.y + radius * sin(angle).toFloat()
        
        drawLine(
            color = axisColor,
            start = center,
            end = Offset(endX, endY),
            strokeWidth = 1.dp.toPx()
        )
    }
}
