package icesi.edu.co.fitscan.features.common.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
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
    val barSpace = 8.dp
    val maxVal = (currentWeek + lastWeek).maxOrNull() ?: 1f
    val barCount = 7
    val totalBarSpaces = (barCount - 1) * barSpace.value
    BoxWithConstraints(modifier = modifier.fillMaxSize().padding(all=8.dp), contentAlignment = Alignment.BottomStart) {
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
                        Box(
                            modifier = Modifier
                                .height(((lastWeek.getOrNull(i) ?: 0f) / maxVal * maxBarHeight.value).dp)
                                .width(barWidth)
                                .background(MaterialTheme.colorScheme.chartSecondary, shape = RoundedCornerShape(4.dp))
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        // Current week bar (right)
                        Box(
                            modifier = Modifier
                                .height(((currentWeek.getOrNull(i) ?: 0f) / maxVal * maxBarHeight.value).dp)
                                .width(barWidth)
                                .background(MaterialTheme.colorScheme.chartTertiary, shape = RoundedCornerShape(4.dp))
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(dayLabels.getOrNull(i) ?: "", color = MaterialTheme.colorScheme.onBackground, fontSize = 12.sp)
                }
                if (i < barCount - 1) Spacer(modifier = Modifier.width(barSpace))
            }
        }
    }
}
