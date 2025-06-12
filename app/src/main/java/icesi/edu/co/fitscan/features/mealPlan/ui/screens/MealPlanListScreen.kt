package icesi.edu.co.fitscan.features.mealPlan.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.painterResource
import icesi.edu.co.fitscan.R
import androidx.lifecycle.viewmodel.compose.viewModel
import icesi.edu.co.fitscan.features.mealPlan.data.dto.MealPlanDto
import icesi.edu.co.fitscan.features.mealPlan.data.repositories.MealPlanRepository
import icesi.edu.co.fitscan.features.mealPlan.data.datasources.IMealPlanDataSource
import icesi.edu.co.fitscan.features.common.data.remote.RetrofitInstance
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import icesi.edu.co.fitscan.features.mealPlan.ui.viewmodel.MealPlanListViewModel
import icesi.edu.co.fitscan.features.mealPlan.data.usecases.GetMealPlansUseCase
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

@Composable
fun MealPlanListScreen(navController: androidx.navigation.NavController) {
    // ViewModel setup (replace with DI if available)
    val dataSource = remember { RetrofitInstance.create(IMealPlanDataSource::class.java) }
    val repository = remember { MealPlanRepository(dataSource) }
    val useCase = remember { GetMealPlansUseCase(repository) }
    val viewModel: MealPlanListViewModel = viewModel(factory = viewModelFactory {
        initializer { MealPlanListViewModel(repository, useCase) }
    })
    val myPlans by viewModel.myPlans.collectAsState()
    val popularPlans by viewModel.popularPlans.collectAsState()

    LaunchedEffect(Unit) { viewModel.loadData() }

    val backgroundColor = MaterialTheme.colorScheme.background
    val cardColor = MaterialTheme.colorScheme.surfaceVariant
    val accentColor = MaterialTheme.colorScheme.primary
    val textColor = MaterialTheme.colorScheme.onBackground
    val secondaryTextColor = MaterialTheme.colorScheme.onSurfaceVariant

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Alimentación",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = textColor
                    )
                    Spacer(Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(accentColor, shape = RoundedCornerShape(50))
                            .padding(0.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = { navController.navigate("create_meal_plan") },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_add),
                                contentDescription = "Agregar",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
            item {
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Mis planes", color = textColor, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.weight(1f))
                }
            }
            items(myPlans) { plan ->
                MealPlanCard(plan, onDetail = { /* TODO: Detail */ }, cardColor, accentColor, textColor, secondaryTextColor)
                    //onEdit = { /* TODO: Edit */ }, onDetail = { /* TODO: Detail */ }, cardColor, accentColor, textColor, secondaryTextColor)
            }
        }
    }
}

@Composable
fun MealPlanCard(
    plan: MealPlanDto,
    //onEdit: (() -> Unit)? = null,
    onDetail: () -> Unit,
    cardColor: Color,
    accentColor: Color,
    textColor: Color,
    secondaryTextColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(plan.name, color = accentColor, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(Modifier.height(4.dp))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Progreso de hoy", color = textColor, fontSize = 13.sp)
                Spacer(Modifier.weight(1f))
                Text(
                    "- / - kcal", // TODO: Replace with real progress/calories
                    color = textColor,
                    fontSize = 12.sp
                )
            }
            Spacer(Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = 0f, // TODO: Replace with real progress
                color = accentColor,
                trackColor = secondaryTextColor,
                modifier = Modifier.fillMaxWidth().height(6.dp)
            )
            Spacer(Modifier.height(4.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Proteína\n- g", color = textColor, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Text("Carbohidratos\n- g", color = textColor, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Text("Grasas\n- g", color = textColor, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                /*if (onEdit != null) {
                    OutlinedButton(
                        onClick = onEdit,
                        modifier = Modifier.padding(end = 8.dp),
                        border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp)
                    ) {
                        Text("Editar", color = accentColor)
                    }
                }*/
                Button(
                    onClick = onDetail,
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                ) {
                    Text("Detalles", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MealPlanListScreenPreview() {
    // Mock theme and sample data for preview
    MaterialTheme {
        val navController = rememberNavController()
        // Preview does not use navigation
        MealPlanListScreen(navController = navController)
    }
}
