package icesi.edu.co.fitscan.features.workout.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import icesi.edu.co.fitscan.R
import icesi.edu.co.fitscan.ui.theme.FitScanTheme
import icesi.edu.co.fitscan.ui.theme.greenLess
import icesi.edu.co.fitscan.ui.theme.greyMed
import icesi.edu.co.fitscan.ui.theme.greyStrong

@Composable
fun CreateWorkoutGymScreen() {
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
                .padding(top = 16.dp)
        ) {
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
    }
}

@Preview(showBackground = true)
@Composable
fun CreateWorkoutGymScreenPreview() {
    FitScanTheme {
        CreateWorkoutGymScreen()
    }
}