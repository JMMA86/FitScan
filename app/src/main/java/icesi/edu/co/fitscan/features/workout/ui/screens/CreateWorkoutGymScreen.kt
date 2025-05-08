package icesi.edu.co.fitscan.features.workout.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import icesi.edu.co.fitscan.R
import icesi.edu.co.fitscan.features.common.ui.components.ExerciseList
import icesi.edu.co.fitscan.features.common.ui.components.FitScanButton
import icesi.edu.co.fitscan.features.common.ui.components.FitScanTextField
import icesi.edu.co.fitscan.features.common.ui.components.SectionTitle
import icesi.edu.co.fitscan.features.common.ui.components.SuggestionChip
import icesi.edu.co.fitscan.ui.theme.FitScanTheme
import icesi.edu.co.fitscan.ui.theme.greyStrong

@Composable
fun CreateWorkoutGymScreen() {
    val scrollState = rememberScrollState()
    var workoutName by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }

    val availableExercises = remember { mutableStateListOf("Barbell Rows") }
    val filteredAvailableExercises = availableExercises.filter {
        it.contains(searchQuery, ignoreCase = true)
    }

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
            FitScanTextField(
                value = workoutName,
                onValueChange = { workoutName = it },
                placeholder = "Ingresa nombre del entrenamiento",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            SectionTitle("Agregar ejercicios")

            Spacer(modifier = Modifier.height(12.dp))

            FitScanTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = "Buscar ejercicios...",
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = null,
                        tint = Color.Gray
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar todos los ejercicios disponibles dinámicamente
            ExerciseList(
                exercises = filteredAvailableExercises,
                exerciseData = exerciseData,
                showAddButton = true,
                onAdd = { name ->
                    if (!addedExercises.contains(name)) {
                        addedExercises.add(name)
                        availableExercises.remove(name)
                    }
                },
                onSetsChange = { name, newSets ->
                    val current = exerciseData[name]
                    if (current != null) exerciseData[name] = newSets to current.second
                },
                onRepsChange = { name, newReps ->
                    val current = exerciseData[name]
                    if (current != null) exerciseData[name] = current.first to newReps
                }
            )

            SectionTitle("Sugerencias de IA")

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

            ExerciseList(
                exercises = addedExercises,
                exerciseData = exerciseData,
                showAddButton = false,
                onRemove = { name ->
                    addedExercises.remove(name)
                    if (name in allSuggestions && !suggestedExercises.contains(name)) {
                        suggestedExercises.add(name)
                    } else if (!availableExercises.contains(name)) {
                        availableExercises.add(name)
                    }
                },
                onSetsChange = { name, newSets ->
                    val current = exerciseData[name]
                    if (current != null) exerciseData[name] = newSets to current.second
                },
                onRepsChange = { name, newReps ->
                    val current = exerciseData[name]
                    if (current != null) exerciseData[name] = current.first to newReps
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            FitScanButton({}, R.drawable.ic_fitness)
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
