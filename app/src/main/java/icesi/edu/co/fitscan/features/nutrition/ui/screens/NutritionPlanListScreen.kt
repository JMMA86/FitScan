package icesi.edu.co.fitscan.features.nutrition.ui.screens

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
import icesi.edu.co.fitscan.domain.model.MealPlan
import androidx.compose.ui.tooling.preview.Preview
import icesi.edu.co.fitscan.domain.model.Meal
import icesi.edu.co.fitscan.domain.model.MealType
import androidx.compose.ui.res.painterResource
import icesi.edu.co.fitscan.R
import icesi.edu.co.fitscan.ui.theme.*
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import icesi.edu.co.fitscan.features.common.ui.components.FitScanNavBar

@Composable
fun NutritionPlanListScreen(
    myPlans: List<MealPlan>,
    popularPlans: List<MealPlan>,
    onEdit: (MealPlan) -> Unit,
    onDetail: (MealPlan) -> Unit,
    onAdd: () -> Unit,
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(greyStrong)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Alimentación",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Color.White
                )
                Spacer(Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(navBarGreen, shape = RoundedCornerShape(50))
                        .padding(0.dp),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = onAdd, modifier = Modifier.size(40.dp)) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add),
                            contentDescription = "Agregar",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Mis planes", color = Color.White, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_forward),
                    contentDescription = null,
                    tint = Color.White
                )
            }
            myPlans.forEach { plan ->
                MealPlanCard(plan, onEdit = { onEdit(plan) }, onDetail = { onDetail(plan) })
            }
            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Planes populares", color = Color.White, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_forward),
                    contentDescription = null,
                    tint = Color.White
                )
            }
            popularPlans.forEach { plan ->
                MealPlanCard(plan, onEdit = null, onDetail = { onDetail(plan) })
            }
        }
        // Barra de navegación inferior
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {
            FitScanNavBar(navController = navController)
        }
    }
}

@Composable
fun MealPlanCard(plan: MealPlan, onEdit: (() -> Unit)?, onDetail: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = greyMed),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(plan.title, color = navBarGreen, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(Modifier.height(4.dp))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Progreso de hoy", color = Color.White, fontSize = 13.sp)
                Spacer(Modifier.weight(1f))
                Text(
                    "${plan.progress} / ${plan.caloriesGoal} kcal",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
            Spacer(Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = if (plan.caloriesGoal > 0) (plan.progress / plan.caloriesGoal.toFloat()).coerceIn(0f, 1f) else 0f,
                color = navBarGreen,
                trackColor = greyTrueLight,
                modifier = Modifier.fillMaxWidth().height(6.dp)
            )
            Spacer(Modifier.height(4.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Proteína\n${plan.protein}g", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Text("Carbohidratos\n${plan.carbs}g", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Text("Grasas\n${plan.fats}g", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                if (onEdit != null) {
                    OutlinedButton(
                        onClick = onEdit,
                        modifier = Modifier.padding(end = 8.dp),
                        border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp)
                    ) {
                        Text("Editar", color = navBarGreen)
                    }
                }
                Button(
                    onClick = onDetail,
                    colors = ButtonDefaults.buttonColors(containerColor = navBarGreen)
                ) {
                    Text("Detalles", color = Color.White)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NutritionPlanListScreenPreview() {
    val navController = rememberNavController()
    val sampleMyPlans = listOf(
        MealPlan(
            id = "1",
            title = "Mi Plan Personal",
            description = "Plan personalizado para mis objetivos",
            author = "Tú",
            tags = listOf("Personalizado"),
            meals = listOf(
                Meal(
                    type = MealType.BREAKFAST,
                    foods = listOf("Avena con frutas", "Yogur griego"),
                    name = "Desayuno energético",
                    description = "Desayuno rico en proteínas y fibra"
                )
            ),
            caloriesGoal = 2000,
            progress = 1500,
            protein = 120,
            carbs = 200,
            fats = 65
        )
    )

    val samplePopularPlans = listOf(
        MealPlan(
            id = "2",
            title = "Plan Fitness Pro",
            description = "Plan diseñado por expertos en nutrición deportiva",
            author = "Dr. Carlos Ruiz",
            tags = listOf("Fitness", "Proteico"),
            meals = listOf(
                Meal(
                    type = MealType.BREAKFAST,
                    foods = listOf("Batido de proteínas", "Huevos revueltos"),
                    name = "Desayuno proteico",
                    description = "Alto contenido en proteínas para desarrollo muscular"
                )
            ),
            caloriesGoal = 2500,
            progress = 1800,
            protein = 150,
            carbs = 250,
            fats = 80
        )
    )

    NutritionPlanListScreen(
        myPlans = sampleMyPlans,
        popularPlans = samplePopularPlans,
        onEdit = {},
        onDetail = {},
        onAdd = {},
        navController = navController
    )
}