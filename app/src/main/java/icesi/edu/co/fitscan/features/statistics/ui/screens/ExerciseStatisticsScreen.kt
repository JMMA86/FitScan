package icesi.edu.co.fitscan.features.statistics.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import icesi.edu.co.fitscan.R
import icesi.edu.co.fitscan.features.statistics.ui.components.TrainedHoursChart
import icesi.edu.co.fitscan.features.statistics.ui.viewmodel.ExerciseStatisticsViewModel

@Composable
fun ExerciseStatisticsScreen(
    viewModel: ExerciseStatisticsViewModel = viewModel(),
    navController: NavController = rememberNavController()
) {
    val pointsData = viewModel.pointsData.collectAsState().value
    val labels = viewModel.labels.collectAsState().value
    val isLoading = viewModel.isLoading.collectAsState().value
    val distanceKm = 12.4f // TODO: Replace with real data from viewmodel
    val weightLost = 2.3f // TODO: Replace with real data from viewmodel
    val weightLostProgress = 0.8f // TODO: Replace with real data from viewmodel
    val caloriesAreaData = listOf(0.2f, 0.5f, 1f, 0.7f, 0.4f, 0.6f) // TODO: Replace with real data
    val weightAreaData = listOf(0.1f, 0.4f, 0.8f, 0.6f, 0.3f, 0.5f) // TODO: Replace with real data

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Top Navigation Bar
        Surface(
            color = Color.Black,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { /* Implement back navigation here */ }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Text(
                    text = "Estadísticas de rutina",
                    color = Color.White,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                IconButton(onClick = { /* Implement menu action here */ }) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "Menu", tint = Color.White)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Progress Visual Card
        ProgressVisualCard(onClick = {
            navController.navigate("progress_photos")
        })
        Spacer(modifier = Modifier.height(12.dp))
        // Distance Card
        DistanceCard(distanceKm = distanceKm, onClick = {
            navController.navigate("distance_details")
        })
        Spacer(modifier = Modifier.height(20.dp))

        // Health Metrics Section
        Text(
            text = "Métricas de salud",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        HealthMetricCard(weightLost = weightLost, progress = weightLostProgress)
        Spacer(modifier = Modifier.height(20.dp))

        // Hours Worked Chart Section
        Text(
            text = "Horas trabajadas vs La última semana",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .padding(horizontal = 8.dp)
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                StackedBarChart(
                    currentWeek = listOf(2f, 3f, 4f, 2.5f, 3.5f, 4.2f, 3.8f),
                    lastWeek = listOf(1.5f, 2.2f, 3.1f, 2.0f, 2.8f, 3.0f, 2.7f),
                    labels = listOf("L", "M", "X", "J", "V", "S", "D")
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        // Bottom Grid: Calorías y Peso Movido
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CaloriesGoalCard(
                modifier = Modifier.weight(1f),
                areaData = caloriesAreaData
            )
            WeightMovedGoalCard(
                modifier = Modifier.weight(1f),
                areaData = weightAreaData
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ProgressVisualCard(onClick: () -> Unit = {}) {
    Surface(
        color = Color(0xFF333333),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(Color.White, shape = RoundedCornerShape(50))
                    .padding(10.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_run),
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Progreso visual", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text("Guarda tus fotos!", color = Color.White, fontSize = 14.sp)
            }
            Box(
                modifier = Modifier
                    .background(Color(0xFF00FF7F), shape = RoundedCornerShape(50))
                    .padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun DistanceCard(distanceKm: Float = 5f, onClick: () -> Unit = {}) {
    Surface(
        color = Color(0xFF333333),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(Color.White, shape = RoundedCornerShape(50))
                    .padding(10.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_run),
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Distancia recorrida", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text("${distanceKm}km ~ 6 veces el Burj Khalifa!", color = Color.White, fontSize = 14.sp)
            }
            Box(
                modifier = Modifier
                    .background(Color(0xFF00FF7F), shape = RoundedCornerShape(50))
                    .padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun HealthMetricCard(weightLost: Float = 2.3f, progress: Float = 0.8f) {
    Surface(
        color = Color(0xFF333333),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Peso perdido", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("${weightLost}kg esta semana. Ya casi!", color = Color.White, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(12.dp))
            // Progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .background(Color(0xFF008000), shape = RoundedCornerShape(8.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .height(16.dp)
                        .background(Color(0xFF00FF7F), shape = RoundedCornerShape(8.dp))
                )
            }
        }
    }
}

@Composable
fun CaloriesGoalCard(modifier: Modifier = Modifier, areaData: List<Float> = listOf(0.2f, 0.5f, 1f, 0.7f, 0.4f, 0.6f)) {
    Surface(
        color = Color(0xFF333333),
        shape = RoundedCornerShape(18.dp),
        modifier = modifier.height(140.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Calorías", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("Goal", color = Color.White, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            AreaChart(
                data = areaData,
                color = Color(0xFFADD8E6),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .background(Color(0xFF00FF7F), shape = RoundedCornerShape(8.dp))
            ) {}
        }
    }
}

@Composable
fun WeightMovedGoalCard(modifier: Modifier = Modifier, areaData: List<Float> = listOf(0.1f, 0.4f, 0.8f, 0.6f, 0.3f, 0.5f)) {
    Surface(
        color = Color(0xFF333333),
        shape = RoundedCornerShape(18.dp),
        modifier = modifier.height(140.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Peso Movido", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("Goal", color = Color.White, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            AreaChart(
                data = areaData,
                color = Color(0xFF8B0000),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .background(Color(0xFFFF0000), shape = RoundedCornerShape(8.dp))
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

@Composable
fun StackedBarChart(currentWeek: List<Float>, lastWeek: List<Float>, labels: List<String>, modifier: Modifier = Modifier) {
    // Simple stacked bar chart for 7 days
    val barWidth = 20.dp
    val maxVal = (currentWeek + lastWeek).maxOrNull() ?: 1f
    val barSpace = 16.dp
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomStart
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Bottom
        ) {
            for (i in 0 until 7) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .height(180.dp)
                            .width(barWidth),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        // Last week bar
                        Box(
                            modifier = Modifier
                                .height((lastWeek.getOrNull(i) ?: 0f) / maxVal * 180.dp)
                                .width(barWidth)
                                .background(Color(0xFF8B8B45), shape = RoundedCornerShape(6.dp))
                        )
                        // Current week bar (on top)
                        Box(
                            modifier = Modifier
                                .height((currentWeek.getOrNull(i) ?: 0f) / maxVal * 180.dp)
                                .width(barWidth)
                                .background(Color(0xFFADD8E6), shape = RoundedCornerShape(6.dp))
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(labels.getOrNull(i) ?: "", color = Color.White, fontSize = 12.sp)
                }
                if (i < 6) Spacer(modifier = Modifier.width(barSpace))
            }
        }
    }
}

@Composable
@Preview
fun PreviewExerciseStatisticsScreen() {
    ExerciseStatisticsScreen()
}