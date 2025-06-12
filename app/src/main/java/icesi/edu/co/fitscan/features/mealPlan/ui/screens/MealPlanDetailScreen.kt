package icesi.edu.co.fitscan.features.mealPlan.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import icesi.edu.co.fitscan.features.mealPlan.data.datasources.IMealPlanDataSource
import icesi.edu.co.fitscan.features.mealPlan.data.repositories.MealPlanRepository
import icesi.edu.co.fitscan.features.mealPlan.data.usecases.GetMealPlanByIdUseCase
import icesi.edu.co.fitscan.features.mealPlan.ui.viewmodel.MealPlanDetailViewModel
import icesi.edu.co.fitscan.features.common.data.remote.RetrofitInstance
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

@Composable
fun MealPlanDetailScreen(mealPlanId: String) {
    val dataSource = RetrofitInstance.create(IMealPlanDataSource::class.java)
    val repository = MealPlanRepository(dataSource)
    val useCase = GetMealPlanByIdUseCase(repository)
    val viewModel: MealPlanDetailViewModel = viewModel(factory = viewModelFactory {
        initializer { MealPlanDetailViewModel(useCase) }
    })
    val mealPlan by viewModel.mealPlan.collectAsState()
    val meals by viewModel.meals.collectAsState()

    LaunchedEffect(mealPlanId) {
        viewModel.loadMealPlanAndMeals(mealPlanId, repository)
    }

    val backgroundColor = MaterialTheme.colorScheme.background
    val textColor = MaterialTheme.colorScheme.onBackground
    val secondaryTextColor = MaterialTheme.colorScheme.onSurfaceVariant
    val accentColor = MaterialTheme.colorScheme.primary

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        mealPlan?.let { plan ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                item {
                    Text(
                        plan.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = accentColor
                    )
                    Spacer(Modifier.height(8.dp))
                    if (!plan.description.isNullOrBlank()) {
                        Text(plan.description, color = textColor, fontSize = 16.sp)
                        Spacer(Modifier.height(16.dp))
                    }
                }
                // Show meals if present via relation
                if (meals.isNotEmpty()) {
                    item {
                        Text("Comidas", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = accentColor)
                        Spacer(Modifier.height(8.dp))
                    }
                    items(meals) { meal ->
                        Column(Modifier.padding(bottom = 16.dp)) {
                            Text(meal.name, fontWeight = FontWeight.SemiBold, color = textColor, fontSize = 16.sp)
                            meal.meal_type?.let {
                                Text(it, color = secondaryTextColor, fontSize = 14.sp)
                            }
                            meal.description?.let {
                                if (it.isNotBlank()) Text(it, color = secondaryTextColor, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }
        } ?: Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = accentColor)
        }
    }
} 