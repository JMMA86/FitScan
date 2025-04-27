package icesi.edu.co.fitscan.features.nutrition.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import icesi.edu.co.fitscan.R
import icesi.edu.co.fitscan.ui.theme.FitScanTheme

@Composable
fun NutritionPlanListScreen(
    onEditPlanClick: () -> Unit = {},
    onPlanDetailsClick: () -> Unit = {},
    onNavigateToPlans: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToRoutines: () -> Unit = {},
    onNavigateToStats: () -> Unit = {}
) {
    Scaffold(
        containerColor = Color.Black,
        contentColor = Color.White,
        bottomBar = {
            BottomNavigationBar(
                onHomeClick = onNavigateToHome,
                onRoutinesClick = onNavigateToRoutines,
                onMealPlansClick = {},
                onStatsClick = onNavigateToStats
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Alimentación",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            item {
                SectionHeader(title = "Mis planes", onClick = onNavigateToPlans)
            }

            item {
                MealPlanCard(
                    title = "Mi plan fitness",
                    progress = 1850,
                    total = 2200,
                    protein = "120g",
                    carbs = "180g",
                    fats = "65g",
                    actionText = "Editar",
                    onActionClick = onEditPlanClick,
                    onDetailsClick = onPlanDetailsClick
                )
            }

            item {
                SectionHeader(title = "Planes populares", onClick = onNavigateToPlans)
            }

            item {
                MealPlanCard(
                    title = "Plan semanal tranquilo y balanceado",
                    progress = 0,
                    total = 2200,
                    protein = "420g",
                    carbs = "280g",
                    fats = "15g",
                    actionText = "Detalles",
                    onDetailsClick = onPlanDetailsClick
                )
            }

            item {
                MealPlanCard(
                    title = "Plan mensual vegano",
                    progress = 1850,
                    total = 2200,
                    protein = "Home",
                    carbs = "Rutinas",
                    fats = "Meal Plans",
                    actionText = "Detalles",
                    onDetailsClick = onPlanDetailsClick
                )
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        TextButton(onClick = onClick) {
            Text(
                text = "",
                color = MaterialTheme.colorScheme.onSurface
            )
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Ver todos",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun MealPlanCard(
    title: String,
    progress: Int,
    total: Int,
    protein: String,
    carbs: String,
    fats: String,
    actionText: String,
    onActionClick: (() -> Unit)? = null,
    onDetailsClick: () -> Unit
) {
    val progressPercentage = progress.toFloat() / total.toFloat()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E),
            contentColor = Color.White
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                //fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Progreso de hoy",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray
                )
                Text(
                    text = "$progress / $total kcal",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            LinearProgressIndicator(
                progress = progressPercentage,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = Color(0xFF2E2E2E)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Macronutrients row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Proteínas",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.LightGray
                    )
                    Text(
                        text = protein,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Carbohidratos",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.LightGray
                    )
                    Text(
                        text = carbs,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Grasas",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.LightGray
                    )
                    Text(
                        text = fats,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Actions row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                if (onActionClick != null) {
                    TextButton(onClick = onActionClick) {
                        Text(
                            text = actionText,
                            color = Color(0xFF4CAF50)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                }

                Button(
                    onClick = onDetailsClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Detalles")
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    onHomeClick: () -> Unit,
    onRoutinesClick: () -> Unit,
    onMealPlansClick: () -> Unit,
    onStatsClick: () -> Unit
) {
    NavigationBar(
        containerColor = Color.Black,
        contentColor = Color.White
    ) {
        NavigationBarItem(
            selected = false,
            onClick = onHomeClick,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_home),
                    contentDescription = "Home",
                    tint = Color.White
                )
            },
            label = {
                Text(
                    text = "Home",
                    color = Color.White
                )
            }
        )
        NavigationBarItem(
            selected = false,
            onClick = onRoutinesClick,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_routines),
                    contentDescription = "Rutinas",
                    tint = Color.White
                )
            },
            label = {
                Text(
                    text = "Rutinas",
                    color = Color.White
                )
            }
        )
        NavigationBarItem(
            selected = true,
            onClick = onMealPlansClick,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_meal_plans),
                    contentDescription = "Meal Plans",
                    tint = Color.White
                )
            },
            label = {
                Text(
                    text = "Meal Plans",
                    color = Color.White
                )
            }
        )
        NavigationBarItem(
            selected = false,
            onClick = onStatsClick,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_stats),
                    contentDescription = "Estadísticas",
                    tint = Color.White
                )
            },
            label = {
                Text(
                    text = "Estadísticas",
                    color = Color.White
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NutritionPlanListPreview() {
    FitScanTheme(
        darkTheme = true,
        dynamicColor = false
    ) {
        NutritionPlanListScreen()
    }
}