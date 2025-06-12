package icesi.edu.co.fitscan.features.statistics.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
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
    val surfaceColor = MaterialTheme.colorScheme.outline
    val textColor = MaterialTheme.colorScheme.onBackground
    val textMeasurer = rememberTextMeasurer()
    
    var selectedPoint by remember { mutableStateOf<PointInfo?>(null) }
    
    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(muscleGroupProgress) {
                    detectTapGestures { offset ->
                        selectedPoint = findTappedPoint(
                            offset,
                            muscleGroupProgress,
                            size.width.toFloat(),
                            size.height.toFloat(),
                            maxValue
                        )
                    }
                }
        ) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val radius = (size.minDimension / 2f) * 0.6f // Reduced to make space for labels
            
            if (muscleGroupProgress.isNotEmpty()) {
                // Draw background grid circles
                drawGrid(center, radius, surfaceColor)
                
                // Draw polygon based on muscle group progress
                val points = drawProgressPolygon(
                    muscleGroupProgress = muscleGroupProgress,
                    center = center,
                    radius = radius,
                    maxValue = maxValue,
                    fillColor = secondaryColor,
                    strokeColor = primaryColor,
                    selectedPoint = selectedPoint
                )
                
                // Draw axis lines
                drawAxisLines(
                    muscleGroupProgress = muscleGroupProgress,
                    center = center,
                    radius = radius,
                    axisColor = surfaceColor
                )
                
                // Draw labels outside the polygon
                drawLabels(
                    muscleGroupProgress = muscleGroupProgress,
                    center = center,
                    radius = radius * 1.4f, // Place labels outside
                    textMeasurer = textMeasurer,
                    textColor = textColor
                )
            }
        }
        
        // Tooltip popup
        selectedPoint?.let { pointInfo ->
            Popup(
                onDismissRequest = { selectedPoint = null },
                properties = PopupProperties(focusable = true)
            ) {
                TooltipCard(
                    pointInfo = pointInfo,
                    onDismiss = { selectedPoint = null }
                )
            }
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
    strokeColor: androidx.compose.ui.graphics.Color,
    selectedPoint: PointInfo?
): List<Offset> {
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
        points.forEachIndexed { index, point ->
            val isSelected = selectedPoint?.muscleGroup?.id == muscleGroupProgress[index].muscleGroup.id
            drawCircle(
                color = if (isSelected) strokeColor.copy(alpha = 0.8f) else strokeColor,
                radius = if (isSelected) 8.dp.toPx() else 6.dp.toPx(),
                center = point
            )
            // Draw inner circle for selected point
            if (isSelected) {
                drawCircle(
                    color = androidx.compose.ui.graphics.Color.White,
                    radius = 4.dp.toPx(),
                    center = point
                )
            }
        }
    }
    
    return points
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

private fun DrawScope.drawLabels(
    muscleGroupProgress: List<MuscleGroupProgress>,
    center: Offset,
    radius: Float,
    textMeasurer: TextMeasurer,
    textColor: androidx.compose.ui.graphics.Color
) {
    val angleStep = 2 * PI / muscleGroupProgress.size
    val textStyle = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        color = textColor
    )
    
    muscleGroupProgress.forEachIndexed { index, progress ->
        val angle = angleStep * index - PI / 2 // Start from top
        val labelX = center.x + radius * cos(angle).toFloat()
        val labelY = center.y + radius * sin(angle).toFloat()
        
        val textLayoutResult = textMeasurer.measure(progress.muscleGroup.name, textStyle)
        val textSize = textLayoutResult.size
        
        // Adjust text position based on angle to avoid overlapping with chart
        val offsetX = when {
            cos(angle) > 0.5 -> 8.dp.toPx() // Right side
            cos(angle) < -0.5 -> -textSize.width - 8.dp.toPx() // Left side
            else -> -textSize.width / 2f // Center
        }
        
        val offsetY = when {
            sin(angle) > 0.5 -> 8.dp.toPx() // Bottom
            sin(angle) < -0.5 -> -textSize.height - 8.dp.toPx() // Top
            else -> -textSize.height / 2f // Center
        }
        
        drawText(
            textLayoutResult = textLayoutResult,
            topLeft = Offset(
                labelX + offsetX,
                labelY + offsetY
            )
        )
    }
}

private fun findTappedPoint(
    tapOffset: Offset,
    muscleGroupProgress: List<MuscleGroupProgress>,
    canvasWidth: Float,
    canvasHeight: Float,
    maxValue: Float
): PointInfo? {
    val center = Offset(canvasWidth / 2f, canvasHeight / 2f)
    val radius = (minOf(canvasWidth, canvasHeight) / 2f) * 0.6f
    val angleStep = 2 * PI / muscleGroupProgress.size
    val touchRadius = 24.dp.value // Touch target size
    
    muscleGroupProgress.forEachIndexed { index, progress ->
        val angle = angleStep * index - PI / 2
        val progressRadius = radius * (progress.progressPercentage / maxValue).coerceIn(0f, 1f)
        
        val pointX = center.x + progressRadius * cos(angle).toFloat()
        val pointY = center.y + progressRadius * sin(angle).toFloat()
        val point = Offset(pointX, pointY)
        
        val distance = sqrt((tapOffset.x - point.x).pow(2) + (tapOffset.y - point.y).pow(2))
        if (distance <= touchRadius) {
            return PointInfo(
                muscleGroup = progress.muscleGroup,
                progressPercentage = progress.progressPercentage,
                position = point
            )
        }
    }
    
    return null
}

@Composable
private fun TooltipCard(
    pointInfo: PointInfo,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier.clickable { onDismiss() },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = pointInfo.muscleGroup.name,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${(pointInfo.progressPercentage * 100).toInt()}% progreso",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

private data class PointInfo(
    val muscleGroup: icesi.edu.co.fitscan.domain.model.MuscleGroup,
    val progressPercentage: Float,
    val position: Offset
)
