package icesi.edu.co.fitscan.features.common.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import java.time.LocalDate
import androidx.compose.foundation.layout.BoxWithConstraints
import icesi.edu.co.fitscan.ui.theme.chartSecondary
import icesi.edu.co.fitscan.ui.theme.chartTertiary

@Composable
fun StackedBarChart(
    currentWeek: List<Float>,
    lastWeek: List<Float>,
    labels: List<String>,
    modifier: Modifier = Modifier
) {
    var selectedBar by remember { mutableStateOf<BarInfo?>(null) }
    
    Column(modifier = modifier.fillMaxSize()) {
        // Legend
        ChartLegend(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        
        // Chart
        Box(modifier = Modifier.weight(1f)) {
            StackedBarChartContent(
                currentWeek = currentWeek,
                lastWeek = lastWeek,
                labels = labels,
                onBarClick = { barInfo -> selectedBar = barInfo },
                modifier = Modifier.fillMaxSize()
            )
            
            // Tooltip popup
            selectedBar?.let { barInfo ->
                Popup(
                    onDismissRequest = { selectedBar = null },
                    properties = PopupProperties(focusable = true)
                ) {
                    TooltipCard(
                        barInfo = barInfo,
                        onDismiss = { selectedBar = null }
                    )
                }
            }
        }
    }
}

@Composable
private fun ChartLegend(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LegendItem(
            color = MaterialTheme.colorScheme.chartTertiary,
            label = "Esta semana"
        )
        Spacer(modifier = Modifier.width(24.dp))
        LegendItem(
            color = MaterialTheme.colorScheme.chartSecondary,
            label = "Semana pasada"
        )
    }
}

@Composable
private fun LegendItem(
    color: Color,
    label: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color = color, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun StackedBarChartContent(
    currentWeek: List<Float>,
    lastWeek: List<Float>,
    labels: List<String>,
    onBarClick: (BarInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    val barSpace = 8.dp
    val maxVal = (currentWeek + lastWeek).maxOrNull() ?: 1f
    val barCount = 7
    val totalBarSpaces = (barCount - 1) * barSpace.value
    
    BoxWithConstraints(
        modifier = modifier.fillMaxSize().padding(all = 8.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        val availableWidth = maxWidth.value
        val availableHeight = maxHeight.value
        val labelHeightPx = 24f // Reserve space for labels and spacing (approx 20-24dp)
        val barHeightPx = (availableHeight - labelHeightPx).coerceAtLeast(0f)
        val barWidthPx = ((availableWidth - totalBarSpaces) / (barCount * 2)).coerceAtLeast(8f)
        val barWidth = barWidthPx.dp
        val maxBarHeight = barHeightPx.dp
        
        val today = LocalDate.now()
        val dayLabels = (6 downTo 0).map { offset ->
            today.minusDays(offset.toLong()).dayOfWeek.name.take(3).replaceFirstChar { it.uppercase() }
        }
        
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Bottom
        ) {
            for (i in 0 until barCount) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        // Last week bar (left)
                        val lastWeekValue = lastWeek.getOrNull(i) ?: 0f
                        Box(
                            modifier = Modifier
                                .height(((lastWeekValue) / maxVal * maxBarHeight.value).dp)
                                .width(barWidth)
                                .background(
                                    MaterialTheme.colorScheme.chartSecondary,
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .clickable {
                                    onBarClick(
                                        BarInfo(
                                            day = dayLabels.getOrNull(i) ?: "",
                                            value = lastWeekValue,
                                            isCurrentWeek = false
                                        )
                                    )
                                }
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        // Current week bar (right)
                        val currentWeekValue = currentWeek.getOrNull(i) ?: 0f
                        Box(
                            modifier = Modifier
                                .height(((currentWeekValue) / maxVal * maxBarHeight.value).dp)
                                .width(barWidth)
                                .background(
                                    MaterialTheme.colorScheme.chartTertiary,
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .clickable {
                                    onBarClick(
                                        BarInfo(
                                            day = dayLabels.getOrNull(i) ?: "",
                                            value = currentWeekValue,
                                            isCurrentWeek = true
                                        )
                                    )
                                }
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = dayLabels.getOrNull(i) ?: "",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 12.sp
                    )
                }
                if (i < barCount - 1) Spacer(modifier = Modifier.width(barSpace))
            }
        }
    }
}

@Composable
private fun TooltipCard(
    barInfo: BarInfo,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier
            .clickable { onDismiss() },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = barInfo.day,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (barInfo.isCurrentWeek) "Esta semana" else "Semana pasada",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${barInfo.value.toInt()} horas",
                color = if (barInfo.isCurrentWeek) 
                    MaterialTheme.colorScheme.chartTertiary 
                else 
                    MaterialTheme.colorScheme.chartSecondary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private data class BarInfo(
    val day: String,
    val value: Float,
    val isCurrentWeek: Boolean
)
