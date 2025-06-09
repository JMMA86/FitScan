package icesi.edu.co.fitscan.features.statistics.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import icesi.edu.co.fitscan.features.common.ui.components.FitScanLineChart
import icesi.edu.co.fitscan.features.common.ui.components.FitScanHeader
import icesi.edu.co.fitscan.features.statistics.ui.viewmodel.StatisticsViewModel
import icesi.edu.co.fitscan.features.statistics.ui.viewmodel.factory.StatisticsViewModelFactory

@Composable
fun DetailedChartsScreen(
    navController: NavController,
    viewModel: StatisticsViewModel = viewModel(factory = StatisticsViewModelFactory())
) {
    val labels = viewModel.labels.collectAsState().value
    val caloriesAreaData = viewModel.caloriesAreaData.collectAsState().value
    val weightAreaData = viewModel.weightAreaData.collectAsState().value
    val currentWeek = viewModel.currentWeek.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        FitScanHeader(
            title = "Gráficas Detalladas",
            showBackIcon = true,
            navController = navController
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            // Calories Chart Section
            Text(
                text = "Calorías Quemadas",
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentAlignment = Alignment.Center
            ) {
                FitScanLineChart(
                    data = caloriesAreaData,
                    labels = listOf("L", "M", "X", "J", "V", "S", "D"),
                    modifier = Modifier.fillMaxSize()
                )
            }
            
            /*
            Spacer(modifier = Modifier.height(32.dp))
            
            // Weight Moved Chart Section
            Text(
                text = "Peso Movido",
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentAlignment = Alignment.Center
            ) {
                FitScanLineChart(
                    data = weightAreaData,
                    labels = listOf("L", "M", "X", "J", "V", "S", "D"),
                    modifier = Modifier.fillMaxSize()
                )
            }
             */
            
        }
    }
}
