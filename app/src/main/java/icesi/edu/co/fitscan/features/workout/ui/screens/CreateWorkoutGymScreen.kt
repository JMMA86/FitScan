package icesi.edu.co.fitscan.features.workout.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
    val scrollState = rememberScrollState()
    var workoutName by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }

    val availableExercises = remember { mutableStateListOf("Barbell Rows") }
    val addedExercises = remember { mutableStateListOf("Deadlifts", "Pull-ups", "Press de hombros") }

    val allSuggestions = listOf("Press de banca", "Sentadillas", "Deadlift", "Push ups")
    val suggestedExercises = remember { mutableStateListOf(*allSuggestions.toTypedArray()) }

    val exerciseData = remember {
        mutableStateMapOf(
            "Barbell Rows" to Pair(4, 12),
            "Deadlifts" to Pair(4, 8),
            "Pull-ups" to Pair(4, 10),
            "Press de hombros" to Pair(4, 12),
            "Press de banca" to Pair(4, 10),
            "Sentadillas" to Pair(4, 12),
            "Deadlift" to Pair(4, 8),
            "Push ups" to Pair(3, 15)
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(greyStrong)
                .padding(top = 16.dp)
        ) {
            OutlinedTextField(
                value = workoutName,
                onValueChange = { workoutName = it },
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

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar ejercicios...", color = Color.Gray) },
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

            availableExercises.forEach { name ->
                val (sets, reps) = exerciseData[name] ?: (4 to 10)
                ExerciseCard(
                    name = name,
                    sets = sets,
                    reps = reps,
                    onRemove = { /* nada */ },
                    onAdd = {
                        if (!addedExercises.contains(name)) {
                            addedExercises.add(name)
                            availableExercises.remove(name)
                        }
                    },
                    showAddButton = true
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Text(
                "Sugerencias de IA",
                color = greenLess,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState)
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                suggestedExercises.forEach { suggestion ->
                    SuggestionChip(
                        suggestion,
                        onClick = {
                            if (!addedExercises.contains(suggestion)) {
                                addedExercises.add(suggestion)
                                suggestedExercises.remove(suggestion)
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            addedExercises.forEach { name ->
                val (sets, reps) = exerciseData[name] ?: (4 to 10)
                ExerciseCard(
                    name = name,
                    sets = sets,
                    reps = reps,
                    onRemove = {
                        addedExercises.remove(name)
                        if (name in allSuggestions && !suggestedExercises.contains(name)) {
                            suggestedExercises.add(name)
                        } else if (!availableExercises.contains(name)) {
                            availableExercises.add(name)
                        }
                    },
                    onAdd = { },
                    showAddButton = false
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { /* Crear entrenamiento */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = greenLess,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_fitness),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Crear entrenamiento", fontSize = 16.sp)
            }
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
