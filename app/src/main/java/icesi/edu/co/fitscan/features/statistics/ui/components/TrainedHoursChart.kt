package icesi.edu.co.fitscan.features.statistics.ui.components

// This file is now deprecated. Use VicoTrainedHoursChart instead for statistics charts.

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import icesi.edu.co.fitscan.ui.theme.*

@Composable
fun TrainedHoursChart(pointsData: List<Point>, labels: List<String>) {

    val xAxisData = AxisData.Builder()
        .labelData { i -> labels.getOrNull(i) ?: "" }
        .axisLineColor(Color.White)
        .axisLabelColor(Color.White)
        .axisLabelAngle(-45f)
        .shouldDrawAxisLineTillEnd(true)
        .build()

    val yAxisData = AxisData.Builder()
        .labelData { i -> pointsData.get(i).y.toInt().toString() }
        .backgroundColor(greyMed)
        .axisLineColor(Color.White)
        .axisLabelColor(Color.White)
        .build()

    val lineChartData = LineChartData(
        /*
        paddingTop = 10.dp,
        bottomPadding = 10.dp,
        containerPaddingEnd = 0.dp,
        gridLines = GridLines(),
         */
        paddingRight = 0.dp,
        backgroundColor = greyMed,
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pointsData,
                    lineStyle = LineStyle(
                        color = greenLess,
                        lineType = LineType.Straight()
                    ),
                    intersectionPoint = IntersectionPoint(
                        color = greenLess
                    ),
                    selectionHighlightPoint = SelectionHighlightPoint(
                        color = greenLess
                    ),
                    shadowUnderLine = ShadowUnderLine(
                        alpha = 0.5f,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                greenLess.copy(alpha = 0.3f),
                                Color.Transparent
                            )
                        )
                    )
                )
            ),
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData
    )

    LineChart(
        lineChartData = lineChartData,
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(greyMed)
            .padding(16.dp)
    )
}