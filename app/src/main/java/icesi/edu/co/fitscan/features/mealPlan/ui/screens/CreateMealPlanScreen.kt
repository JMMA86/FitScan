package icesi.edu.co.fitscan.features.mealPlan.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import icesi.edu.co.fitscan.R
import icesi.edu.co.fitscan.domain.model.MealPlan
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.ui.draw.clip
import icesi.edu.co.fitscan.ui.theme.navBarGreen
import icesi.edu.co.fitscan.features.common.ui.components.FitScanNavBar
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun CreateMealPlanScreen(
    onGeneratePlan: (MealPlan) -> Unit = {},
    navController: NavController
) {
    val scrollState = rememberScrollState()
    var goal by remember { mutableStateOf("") }
    val goalOptions = listOf(
        Pair("Perder peso", R.drawable.ic_goal_weight),
        Pair("Músculo", R.drawable.ic_goal_muscle),
        Pair("Condición", R.drawable.ic_goal_condition)
    )
    val restrictionOptions = listOf("Gluten", "Mariscos", "Soya", "Huevos")
    var selectedRestrictions by remember { mutableStateOf(setOf<String>()) }
    val preferenceOptions = listOf("Vegetarian", "Vegan")
    var preferences by remember { mutableStateOf(mapOf<String, Boolean>("Vegetarian" to false, "Vegan" to false)) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF181F1C))
                .verticalScroll(scrollState)
                .padding(16.dp)
                .padding(bottom = 80.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Regresar",
                    tint = Color.White,
                    modifier = Modifier
                        .clickable { /* Acción regresar */ }
                        .size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Crear plan de alimentación",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Fecha de publicación", color = Color.White, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
            // Selector de meta
            Text("¿Cual es tu meta?", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text("Selecciona tu meta principal", color = Color.White, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                goalOptions.forEach { (option, icon) ->
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (goal == option) navBarGreen else Color(0xFF232B28))
                            .clickable { goal = option }
                            .padding(vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(id = icon),
                            contentDescription = option,
                            tint = if (goal == option) Color(0xFF232B28) else navBarGreen,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(option, color = if (goal == option) Color(0xFF232B28) else Color.White, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Restricciones alimenticias
            Text("Restricciones alimenticias", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("Selecciona alergias o comidas a evitar", color = Color.White, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                restrictionOptions.chunked(2).forEach { rowItems ->
                    Column(Modifier.weight(1f)) {
                        rowItems.forEach { restriction ->
                            Box(
                                modifier = Modifier
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (selectedRestrictions.contains(restriction)) navBarGreen else Color(0xFF232B28))
                                    .clickable {
                                        selectedRestrictions = if (selectedRestrictions.contains(restriction))
                                            selectedRestrictions - restriction else selectedRestrictions + restriction
                                    }
                                    .padding(horizontal = 20.dp, vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    restriction,
                                    color = if (selectedRestrictions.contains(restriction)) Color(0xFF232B28) else Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Preferencias
            Text("Preferencias", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("Selecciona las comidas que te gustan", color = Color.White, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                preferenceOptions.forEach { pref ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF232B28))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_home), // Usa el ícono que prefieras
                            contentDescription = pref,
                            tint = navBarGreen,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(pref, color = Color.White, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                        Switch(
                            checked = preferences[pref] == true,
                            onCheckedChange = { preferences = preferences.toMutableMap().apply { put(pref, it) } },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = navBarGreen,
                                checkedTrackColor = navBarGreen.copy(alpha = 0.5f),
                                uncheckedThumbColor = Color.Gray,
                                uncheckedTrackColor = Color.DarkGray
                            )
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    onGeneratePlan(
                        MealPlan(
                            title = "", // Puedes agregar más campos si lo deseas
                            description = "Plan generado automáticamente",
                            author = "Tú"
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = navBarGreen),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Generar plan alimenticio", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))

        }
        // Barra de navegación alineada al fondo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            FitScanNavBar(navController = navController)
        }
    }
}

@Composable
fun GoalOption(
    text: String,
    selected: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = { onSelect(text) },
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selected == text) Color(0xFF4ADE80) else Color(0xFF232B28),
            contentColor = Color.White
        )
    ) {
        Text(text)
    }
}

@Preview(showBackground = true)
@Composable
fun CreateMealPlanScreenPreview() {
    val navController = rememberNavController()
    CreateMealPlanScreen(
        onGeneratePlan = {},
        navController = navController
    )
}

