package icesi.edu.co.fitscan.features.mealplan.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.style.TextAlign
import icesi.edu.co.fitscan.R
import icesi.edu.co.fitscan.ui.theme.FitScanTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import icesi.edu.co.fitscan.features.mealPlan.ui.viewmodel.CreateMealPlanViewModel
import icesi.edu.co.fitscan.features.mealPlan.data.datasources.IMealPlanDataSource
import icesi.edu.co.fitscan.features.mealPlan.data.repositories.MealPlanRepository
import icesi.edu.co.fitscan.features.mealPlan.data.usecases.CreateMealPlanUseCase
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import icesi.edu.co.fitscan.features.common.data.remote.RetrofitInstance
import icesi.edu.co.fitscan.features.mealPlan.data.dto.FitnessGoalDto
import icesi.edu.co.fitscan.features.mealPlan.data.dto.DietaryRestrictionDto
import icesi.edu.co.fitscan.features.mealPlan.data.dto.DietaryPreferenceDto
import icesi.edu.co.fitscan.features.mealPlan.data.dto.MealDto
import icesi.edu.co.fitscan.features.common.ui.viewmodel.AppState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreateMealPlanScreen(
    customerId: String = AppState.customerId ?: ""
) {
    // ViewModel setup (in real app, use DI or pass from NavGraph)
    val dataSource = remember { RetrofitInstance.create(IMealPlanDataSource::class.java) }
    val repository = remember { MealPlanRepository(dataSource) }
    val useCase = remember { CreateMealPlanUseCase(repository) }
    val viewModel: CreateMealPlanViewModel = viewModel(factory = viewModelFactory {
        initializer { CreateMealPlanViewModel(repository, useCase, customerId) }
    })
    val uiState by viewModel.uiState.collectAsState()
    val goals by viewModel.goals.collectAsState()
    val restrictions by viewModel.restrictions.collectAsState()
    val preferences by viewModel.preferences.collectAsState()
    val meals by viewModel.meals.collectAsState()

    // Estado local para selección
    var selectedGoal by remember { mutableStateOf<FitnessGoalDto?>(null) }
    var selectedRestrictions by remember { mutableStateOf(setOf<DietaryRestrictionDto>()) }
    var selectedPreferences by remember { mutableStateOf(setOf<DietaryPreferenceDto>()) }
    var selectedMeals by remember { mutableStateOf(setOf<MealDto>()) }
    var planName by remember { mutableStateOf("") }
    var planDescription by remember { mutableStateOf("") }

    LaunchedEffect(Unit) { viewModel.loadData() }

    val screenBackgroundColor = MaterialTheme.colorScheme.background
    val primaryTextColor = MaterialTheme.colorScheme.onBackground
    val secondaryTextColor = MaterialTheme.colorScheme.onSurfaceVariant
    val accentColor = MaterialTheme.colorScheme.primary
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = screenBackgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = "Crear plan de alimentación",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = primaryTextColor
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = planName,
                onValueChange = { planName = it },
                label = { Text("Nombre del plan") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = planDescription,
                onValueChange = { planDescription = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("¿Cuál es tu meta?", color = primaryTextColor, fontSize = 16.sp)
            Text("Selecciona tu meta principal", color = secondaryTextColor, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                goals.forEach { goal ->
                    val selected = selectedGoal?.id == goal.id
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (selected) accentColor.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { selectedGoal = goal }
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_scale),
                                contentDescription = null,
                                tint = if (selected) accentColor else secondaryTextColor,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(goal.goal_name, color = if (selected) accentColor else secondaryTextColor, fontSize = 14.sp)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text("Restricciones alimenticias", color = primaryTextColor, fontSize = 16.sp)
            Text("Selecciona alergias o comidas a evitar", color = secondaryTextColor, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                restrictions.forEach { restriction ->
                    val selected = selectedRestrictions.contains(restriction)
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (selected) accentColor.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                selectedRestrictions = if (selected) selectedRestrictions - restriction else selectedRestrictions + restriction
                            }
                    ) {
                        Text(
                            text = restriction.name,
                            color = if (selected) accentColor else secondaryTextColor,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            fontSize = 14.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text("Preferencias", color = primaryTextColor, fontSize = 16.sp)
            Text("Selecciona las comidas que te gustan", color = secondaryTextColor, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                preferences.forEach { pref ->
                    val selected = selectedPreferences.contains(pref)
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (selected) accentColor.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                selectedPreferences = if (selected) selectedPreferences - pref else selectedPreferences + pref
                            }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_vegetarian),
                                contentDescription = null,
                                tint = if (selected) accentColor else secondaryTextColor,
                                modifier = Modifier.size(18.dp).padding(end = 4.dp)
                            )
                            Text(
                                text = pref.name,
                                color = if (selected) accentColor else secondaryTextColor,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text("Comidas disponibles", color = primaryTextColor, fontSize = 16.sp)
            Text("Selecciona las comidas para tu plan", color = secondaryTextColor, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Column {
                meals.forEach { meal ->
                    val selected = selectedMeals.contains(meal)
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (selected) accentColor.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                selectedMeals = if (selected) selectedMeals - meal else selectedMeals + meal
                            }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Checkbox(
                                checked = selected,
                                onCheckedChange = {
                                    selectedMeals = if (selected) selectedMeals - meal else selectedMeals + meal
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = accentColor,
                                    uncheckedColor = secondaryTextColor
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(meal.name, color = primaryTextColor, fontWeight = FontWeight.Bold)
                                Text(meal.description ?: "", color = secondaryTextColor, fontSize = 12.sp)
                                Text("${meal.calories ?: 0} kcal", color = accentColor, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    viewModel.createMealPlan(
                        name = planName,
                        description = planDescription,
                        goal = selectedGoal?.goal_name,
                        mealIds = selectedMeals.mapNotNull { it.id },
                        restrictionIds = selectedRestrictions.mapNotNull { it.id },
                        preferenceIds = selectedPreferences.mapNotNull { it.id }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = accentColor)
            ) {
                Text(
                    text = "Generar plan alimenticio",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    val navController = androidx.navigation.compose.rememberNavController()

    // Feedback dialog for success/error
    if (uiState is icesi.edu.co.fitscan.features.mealPlan.ui.viewmodel.CreateMealPlanUiState.Success || uiState is icesi.edu.co.fitscan.features.mealPlan.ui.viewmodel.CreateMealPlanUiState.Error) {
        val isSuccess = uiState is icesi.edu.co.fitscan.features.mealPlan.ui.viewmodel.CreateMealPlanUiState.Success
        val message = if (isSuccess) "¡Plan de alimentación creado exitosamente!" else (uiState as? icesi.edu.co.fitscan.features.mealPlan.ui.viewmodel.CreateMealPlanUiState.Error)?.message ?: "Error desconocido"
        androidx.compose.ui.window.Dialog(onDismissRequest = {
            viewModel.loadData() // Optionally reload data
            viewModel.resetUiState()
            if (isSuccess) {
                navController.navigate("nutrition_plan_list") {
                    popUpTo("meal") { inclusive = true }
                    launchSingleTop = true
                }
            }
        }) {
            Surface(shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surface) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = if (isSuccess) "¡Éxito!" else "Error", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = message, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        viewModel.loadData()
                        viewModel.resetUiState()
                        if (isSuccess) {
                            navController.navigate("nutrition_plan_list") {
                                popUpTo("meal") { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    }) {
                        Text("OK")
                    }
                }
            }
        }
    }
}

data class GoalOption(val label: String, val iconResId: Int)

@Composable
fun GoalOptionsRow(options: List<GoalOption>) {
    var selectedLabel by remember { mutableStateOf<String?>(null) }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        options.forEach { option ->
            val isSelected = selectedLabel == option.label
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        else MaterialTheme.colorScheme.surfaceVariant
                    )
                    .clickable { selectedLabel = option.label }
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = option.iconResId),
                    contentDescription = option.label,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = option.label,
                    color = if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SelectableChips(options: List<String>) {
    var selectedOptions by remember { mutableStateOf(setOf<String>()) }

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { option ->
            val selected = selectedOptions.contains(option)
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        else MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {
                        selectedOptions = if (selected) {
                            selectedOptions - option
                        } else {
                            selectedOptions + option
                        }
                    }
            ) {
                Text(
                    text = option,
                    color = if (selected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateMealPlanPreview() {
    FitScanTheme {
        CreateMealPlanScreen()
    }
}
