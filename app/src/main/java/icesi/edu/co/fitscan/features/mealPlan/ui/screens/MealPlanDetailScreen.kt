package icesi.edu.co.fitscan.features.mealPlan.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import icesi.edu.co.fitscan.domain.model.MealPlan
import androidx.compose.ui.tooling.preview.Preview
import icesi.edu.co.fitscan.domain.model.Meal
import icesi.edu.co.fitscan.domain.model.MealType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealPlanDetailScreen(
    mealPlan: MealPlan,
    onNavigateBack: () -> Unit = {},
    onStartPlan: () -> Unit = {}
) {
    val screenBackgroundColor = Color(0xFF181F1C)
    val primaryTextColor = Color.White
    val secondaryTextColor = Color.LightGray
    val accentColor = Color(0xFF4ADE80)
    val cardBackgroundColor = Color(0xFF232B28)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(screenBackgroundColor)
    ) {
        TopAppBar(
            title = { Text("Plan alimenticio", color = primaryTextColor) },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = primaryTextColor
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = screenBackgroundColor,
                titleContentColor = primaryTextColor,
                navigationIconContentColor = primaryTextColor
            )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            // Título y autor
            Text(mealPlan.title, fontWeight = FontWeight.Bold, color = primaryTextColor, fontSize = 22.sp)
            Text("Por: ${mealPlan.author}", color = secondaryTextColor, fontSize = 14.sp)
            // Tags
            Row(modifier = Modifier.padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                mealPlan.tags.forEach { tag ->
                    TagChip(text = tag, backgroundColor = cardBackgroundColor, textColor = accentColor)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            // Descripción
            Text(mealPlan.description, color = primaryTextColor, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(16.dp))
            // Lista de comidas
            Text("Comidas del plan", color = accentColor, fontWeight = FontWeight.SemiBold)
            mealPlan.meals.forEach { meal ->
                Text("${meal.type.name.lowercase().replaceFirstChar { it.uppercase() }}:", fontWeight = FontWeight.SemiBold, color = accentColor)
                meal.foods.forEach { food ->
                    Text("- $food", color = primaryTextColor, fontSize = 14.sp)
                }
                Spacer(Modifier.height(6.dp))
            }
            Spacer(modifier = Modifier.height(24.dp))
            // Botón de acción
            Button(
                onClick = onStartPlan,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Empezar plan alimenticio", color = primaryTextColor, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun TagChip(text: String, backgroundColor: Color, textColor: Color) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        modifier = Modifier
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MealPlanDetailScreenPreview() {
    val sampleMealPlan = MealPlan(
        id = "1",
        title = "Plan de pérdida de peso",
        description = "Plan diseñado para perder peso de manera saludable y sostenible. Incluye comidas balanceadas y nutritivas que te ayudarán a alcanzar tus objetivos de manera efectiva.",
        author = "Dr. Juan Pérez",
        tags = listOf("Pérdida de peso", "Saludable", "Bajo en calorías"),
        meals = listOf(
            Meal(
                type = MealType.BREAKFAST,
                foods = listOf("Avena con frutas", "Yogur griego", "Té verde"),
                name = "Desayuno energético",
                description = "Desayuno rico en fibra y antioxidantes"
            ),
            Meal(
                type = MealType.LUNCH,
                foods = listOf("Ensalada de pollo", "Arroz integral", "Vegetales al vapor"),
                name = "Almuerzo balanceado",
                description = "Almuerzo con proteínas magras y carbohidratos complejos"
            ),
            Meal(
                type = MealType.DINNER,
                foods = listOf("Pescado al horno", "Ensalada verde", "Quinoa"),
                name = "Cena ligera",
                description = "Cena rica en proteínas y baja en carbohidratos"
            )
        )
    )
    
    MealPlanDetailScreen(
        mealPlan = sampleMealPlan,
        onNavigateBack = {},
        onStartPlan = {}
    )
} 