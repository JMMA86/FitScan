package icesi.edu.co.fitscan.features.common.ui.components;

import androidx.compose.runtime.Composable;
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import icesi.edu.co.fitscan.ui.theme.greenLess
import icesi.edu.co.fitscan.ui.theme.greyStrong

@Composable
fun FitScanLineChart(
        data: List<Float>,
        labels: List<String>,
        modifier: Modifier = Modifier
) {
    if (data.isEmpty() || labels.isEmpty()) {
        Text(
                text = "No hay datos para mostrar",
                color = Color.White
        )
        return
    }
    Box(
            modifier = modifier
                    .fillMaxSize()
                    .background(greyStrong),
            contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val chartHeight = size.height * 0.7f
            val chartWidth = size.width * 0.8f
            val xStart = size.width * 0.12f
            val yStart = size.height * 0.15f
            val yEnd = yStart + chartHeight
            val xEnd = xStart + chartWidth
            val minValue = data.minOrNull() ?: 0f
            val maxValue = data.maxOrNull() ?: 1f
            val valueRange = (maxValue - minValue).takeIf { it > 0f } ?: 1f
            val pointCount = data.size
            val xStep = if (pointCount > 1) chartWidth / (pointCount - 1) else 0f
            val yTickCount = 5
            val yTickStep = valueRange / (yTickCount - 1)

            // Draw Y axis and ticks
            drawLine(
                    color = greenLess,
                    start = Offset(xStart, yStart),
                    end = Offset(xStart, yEnd),
                    strokeWidth = 3f
            )
            for (i in 0 until yTickCount) {
                val y = yEnd - i * chartHeight / (yTickCount - 1)
                drawLine(
                        color = greenLess,
                        start = Offset(xStart - 8f, y),
                        end = Offset(xStart, y),
                        strokeWidth = 2f
                )
                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                            String.format("%.0f", minValue + i * yTickStep),
                            xStart - 12.dp.toPx(),
                            y + 5.dp.toPx(),
                            android.graphics.Paint().apply {
                        color = android.graphics.Color.WHITE
                        textSize = 28f
                        textAlign = android.graphics.Paint.Align.RIGHT
                    }
                    )
                }
            }

            // Draw X axis and ticks
            drawLine(
                    color = greenLess,
                    start = Offset(xStart, yEnd),
                    end = Offset(xEnd, yEnd),
                    strokeWidth = 3f
            )
            for (i in data.indices) {
                val x = xStart + i * xStep
                drawLine(
                        color = greenLess,
                        start = Offset(x, yEnd),
                        end = Offset(x, yEnd + 8f),
                        strokeWidth = 2f
                )
                // Draw diagonal label
                drawContext.canvas.nativeCanvas.apply {
                    save()
                    rotate(-45f, x, yEnd + 24.dp.toPx())
                    drawText(
                            labels.getOrNull(i) ?: "",
                            x,
                            yEnd + 24.dp.toPx(),
                            android.graphics.Paint().apply {
                        color = android.graphics.Color.WHITE
                        textSize = 28f
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                    )
                    restore()
                }
            }

            // Prepare points
            val points = data.mapIndexed { i, v ->
                    val x = xStart + i * xStep
                val y = yEnd - ((v - minValue) / valueRange) * chartHeight
                Offset(x, y)
            }

            // Draw gradient area below the line
            if (points.size > 1) {
                val areaPath = androidx.compose.ui.graphics.Path().apply {
                    moveTo(points.first().x, yEnd)
                    points.forEach { lineTo(it.x, it.y) }
                    lineTo(points.last().x, yEnd)
                    close()
                }
                drawPath(
                        path = areaPath,
                        brush = Brush.verticalGradient(
                                listOf(greenLess.copy(alpha = 0.4f), greenLess.copy(alpha = 0.1f), Color.Transparent)
                        )
                )
            }

            // Draw the line
            for (i in 0 until points.size - 1) {
                drawLine(
                        color = greenLess,
                        start = points[i],
                        end = points[i + 1],
                        strokeWidth = 5f,
                        cap = StrokeCap.Round
                )
            }
            // Draw points
            for (pt in points) {
                drawCircle(
                        color = greenLess,
                        radius = 10f,
                        center = pt
                )
            }
        }
    }
}