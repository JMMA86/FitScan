package icesi.edu.co.fitscan.features.statistics.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import icesi.edu.co.fitscan.R
import icesi.edu.co.fitscan.features.auth.ui.screens.SectionTitle
import icesi.edu.co.fitscan.features.common.ui.components.StatisticCard
import icesi.edu.co.fitscan.features.common.ui.components.GoalCard
import icesi.edu.co.fitscan.features.common.ui.components.StackedBarChart
import icesi.edu.co.fitscan.features.statistics.ui.viewmodel.ExerciseStatisticsViewModel
import icesi.edu.co.fitscan.navigation.Screen
import icesi.edu.co.fitscan.ui.theme.greyStrong
import icesi.edu.co.fitscan.ui.theme.greenLess

@Composable
fun ExerciseStatisticsScreen(
    viewModel: ExerciseStatisticsViewModel = viewModel(),
    navController: NavController = rememberNavController()
) {
    val labels = viewModel.labels.collectAsState().value
    val isLoading = viewModel.isLoading.collectAsState().value
    val caloriesAreaData = viewModel.caloriesAreaData.collectAsState().value
    val weightAreaData = viewModel.weightAreaData.collectAsState().value
    val currentWeek = viewModel.currentWeek.collectAsState().value
    val lastWeek = viewModel.lastWeek.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(greyStrong)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 8.dp)
    ) {

        SectionTitle("Estadísticas de rutina")

        Spacer(modifier = Modifier.height(16.dp))

        // Progress Visual Card
        StatisticCard(
            title = "Progreso visual",
            subtitle = "¡Próximamente!",
            iconRes = R.drawable.ic_camera,
            onClick = { /* TODO: Implement visual progress screen */ }
        )
        Spacer(modifier = Modifier.height(12.dp))
        // Progreso por ejercicio Card
        StatisticCard(
            title = "Progreso por ejercicio",
            subtitle = "Visualiza tu avance en ejercicios específicos",
            iconRes = R.drawable.ic_run,
            onClick = { navController.navigate(Screen.ExerciseProgress.route) }
        )
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 200.dp, max = 200.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                StackedBarChart(
                    currentWeek = currentWeek,
                    lastWeek = lastWeek,
                    labels = labels,
                    modifier = Modifier.fillMaxSize()
                )
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
                areaData = caloriesAreaData,
                color = greenLess,
                barColor = greenLess,
                modifier = Modifier.weight(1f).heightIn(min = 140.dp, max = 220.dp)
            )
            GoalCard(
                title = "Peso Movido",
                areaData = weightAreaData,
                color = Color.White,
                barColor = Color.White,
                modifier = Modifier.weight(1f).heightIn(min = 140.dp, max = 220.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
@Preview
fun PreviewExerciseStatisticsScreen() {
    ExerciseStatisticsScreen()
}