package icesi.edu.co.fitscan.features.statistics.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import icesi.edu.co.fitscan.R
import icesi.edu.co.fitscan.features.common.ui.components.FitScanHeader
import icesi.edu.co.fitscan.features.common.ui.components.StatisticCard
import icesi.edu.co.fitscan.features.common.ui.components.GoalCard
import icesi.edu.co.fitscan.features.common.ui.components.StackedBarChart
import icesi.edu.co.fitscan.features.statistics.ui.viewmodel.StatisticsViewModel
import icesi.edu.co.fitscan.features.statistics.ui.viewmodel.factory.StatisticsViewModelFactory
import icesi.edu.co.fitscan.navigation.Screen
import icesi.edu.co.fitscan.ui.theme.chartPrimary
import icesi.edu.co.fitscan.ui.theme.iconTint

@Composable
fun StatisticsScreen(
    navController: NavController = rememberNavController()
) {
    val viewModel: StatisticsViewModel = viewModel(factory = StatisticsViewModelFactory())
    val labels = viewModel.labels.collectAsState().value
    val isLoading = viewModel.isLoading.collectAsState().value
    val caloriesAreaData = viewModel.caloriesAreaData.collectAsState().value
    val weightAreaData = viewModel.weightAreaData.collectAsState().value
    val currentWeek = viewModel.currentWeek.collectAsState().value
    val lastWeek = viewModel.lastWeek.collectAsState().value

    Column {
        FitScanHeader(
            title = "Estadísticas de Ejercicio",
            showBackIcon = false,            navController = navController
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 8.dp, vertical = 16.dp)
        ) {

            // Progress Visual Card
            StatisticCard(
                title = "Progreso visual",
                subtitle = "¡Próximamente!",
                iconRes = R.drawable.ic_camera,
                onClick = { navController.navigate(Screen.VisualProgress.route) }
            )
            Spacer(modifier = Modifier.height(12.dp))            // Muscle Group Progress Card
            StatisticCard(
                title = "Progreso por grupo muscular",
                subtitle = "Analiza tu desarrollo muscular con gráficos avanzados",
                iconRes = R.drawable.ic_biceps, // Enhanced with specific fitness icon
                onClick = { navController.navigate(Screen.MuscleGroupProgress.route) }
            )
            Spacer(modifier = Modifier.height(20.dp))// Hours Worked Chart Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate(Screen.DetailedCharts.route) }
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = "Horas trabajadas vs La última semana",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 200.dp, max = 200.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    } else {
                        StackedBarChart(
                            currentWeek = currentWeek,
                            lastWeek = lastWeek,
                            labels = labels,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            // Bottom Grid: Calorías y Peso Movido
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {                
                GoalCard(
                    title = "Calorías",
                    areaData = listOf(2.0f, 5.0f, 3.0f, 4.0f, 6.0f),
                    color = MaterialTheme.colorScheme.chartPrimary,
                    barColor = MaterialTheme.colorScheme.chartPrimary,
                    modifier = Modifier.weight(1f).heightIn(min = 140.dp, max = 220.dp),
                    onClick = { navController.navigate(Screen.DetailedCharts.route) }
                )
                GoalCard(
                    title = "Peso Movido",
                    areaData = listOf(2.0f, 5.0f, 3.0f, 4.0f, 6.0f),
                    color = MaterialTheme.colorScheme.iconTint,
                    barColor = MaterialTheme.colorScheme.iconTint,
                    modifier = Modifier.weight(1f).heightIn(min = 140.dp, max = 220.dp),
                    onClick = { navController.navigate(Screen.ExerciseProgress.route) }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
@Preview
fun StatisticsScreenPreview() {
    StatisticsScreen(rememberNavController())
}
