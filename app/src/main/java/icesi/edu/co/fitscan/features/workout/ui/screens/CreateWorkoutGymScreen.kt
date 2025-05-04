package icesi.edu.co.fitscan.features.workout.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import icesi.edu.co.fitscan.R
import icesi.edu.co.fitscan.ui.theme.FitScanTheme
import icesi.edu.co.fitscan.features.common.ui.components.FitScanNavBar

val greyStrong = Color(0xFF101414)
val greyMed = Color(0xFF302C2C)
val greenLess = Color(0xFF4CAF50)

@Composable
fun CreateWorkoutGymScreen() {
    var selectedTab by remember { mutableStateOf("Gym") }
    val exercises = remember { mutableStateListOf("Deadlifts", "Pull-ups", "Press de hombros") }
    val sets = 4
    val repsMap = remember { mutableStateMapOf(
        "Barbell Rows" to 12,
        "Deadlifts" to 8,
        "Pull-ups" to 10,
        "Press de hombros" to 12
    )}

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(greyStrong)
                .padding(16.dp)
        ) {
            // Barra superior con retroceso y título
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
                    text = "Crear Entrenamiento",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Pestañas Gym y Carrera
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ToggleOption(
                    label = "Gym",
                    selected = selectedTab == "Gym",
                    onSelect = { selectedTab = "Gym" },
                    modifier = Modifier.weight(1f)
                )
                ToggleOption(
                    label = "Carrera",
                    selected = selectedTab == "Carrera",
                    onSelect = { selectedTab = "Carrera" },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Input de nombre
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Ingresa nombre del entrenamiento", color = Color.Gray) },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = greyMed,
                    focusedContainerColor = greyMed,
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Agregar ejercicios",
                color = greenLess,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Barra de búsqueda
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Buscar ejercicios...", color = Color.Gray) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = null,
                        tint = Color.Gray
                    )
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = greyMed,
                    focusedContainerColor = greyMed,
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tarjeta de Barbell Rows (como ejercicio para agregar)
            ExerciseCard(
                name = "Barbell Rows",
                sets = 4,
                reps = 12,
                onRemove = { /* No action */ },
                onAdd = { /* Acción para añadir */ },
                showAddButton = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Sugerencias de IA",
                color = greenLess,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Sugerencias con chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SuggestionChip("Press de banca")
                SuggestionChip("Sentadillas")
                SuggestionChip("Deadlift")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Ejercicios agregados
            exercises.forEach { name ->
                ExerciseCard(
                    name = name,
                    sets = sets,
                    reps = repsMap[name] ?: 10,
                    onRemove = { exercises.remove(name) },
                    onAdd = { /* Acción para añadir sets/reps */ },
                    showAddButton = false
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.weight(1f))

            // Botón de crear entrenamiento
            Button(
                onClick = { /* Acción crear entrenamiento */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = greenLess,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_fitness),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Crear entrenamiento", fontSize = 16.sp)
            }

            // Espacio para la barra de navegación
            Spacer(modifier = Modifier.height(80.dp))
        }

        // Barra de navegación fija en la parte inferior
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(greyStrong)
        ) {
            val navController = rememberNavController()
            FitScanNavBar(navController = navController)
        }
    }
}

@Composable
fun ExerciseCard(
    name: String,
    sets: Int,
    reps: Int,
    onRemove: () -> Unit,
    onAdd: () -> Unit,
    showAddButton: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(greyMed, shape = RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = name,
            color = Color.White,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("SETS", color = Color.Gray, fontSize = 12.sp)
                Text("$sets", color = Color.White, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("REPS", color = Color.Gray, fontSize = 12.sp)
                Text("$reps", color = Color.White, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.width(12.dp))

            if (showAddButton) {
                IconButton(
                    onClick = onAdd,
                    modifier = Modifier
                        .size(30.dp)
                        .background(Color.Transparent)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = "Añadir",
                        tint = greenLess
                    )
                }
            } else {
                IconButton(
                    onClick = onRemove,
                    modifier = Modifier
                        .size(30.dp)
                        .background(Color.Transparent)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_remove),
                        contentDescription = "Eliminar",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}

@Composable
fun ToggleOption(
    label: String,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor = if (selected) greyMed else greyMed
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor)
            .border(
                width = if (selected) 2.dp else 0.dp,
                color = if (selected) greenLess else Color.Transparent,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(onClick = onSelect)
            .padding(vertical = 16.dp, horizontal = 8.dp)
    ) {
        if (label == "Gym") {
            Icon(
                painter = painterResource(id = R.drawable.ic_fitness),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
        } else if (label == "Carrera") {
            Icon(
                painter = painterResource(id = R.drawable.ic_run),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        Text(
            text = label,
            color = Color.White,
            fontSize = 16.sp
        )
    }
}

@Composable
fun SuggestionChip(label: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(greyMed)
            .clickable { /* agregar ejercicio */ }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(label, color = Color.White, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = null,
                tint = greenLess,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateWorkoutGymScreenPreview() {
    FitScanTheme {
        CreateWorkoutGymScreen()
    }
}