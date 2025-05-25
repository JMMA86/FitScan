package icesi.edu.co.fitscan.features.workout.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ExerciseList(
    exercises: List<String>,
    exerciseData: Map<String, Pair<Int, Int>>,
    showAddButton: Boolean,
    modifier: Modifier = Modifier,
    onAdd: (String) -> Unit = {},
    onRemove: (String) -> Unit = {},
    onSetsChange: (String, Int) -> Unit,
    onRepsChange: (String, Int) -> Unit
) {
    Column(modifier = modifier) {
    exercises.forEach { name ->
        val (sets, reps) = exerciseData[name] ?: (4 to 10)
        ExerciseCard(
            name = name,
            sets = sets,
            reps = reps,
            onSetsChange = { newSets -> onSetsChange(name, newSets) },
            onRepsChange = { newReps -> onRepsChange(name, newReps) },
            onAdd = { onAdd(name) },
            onRemove = { onRemove(name) },
            showAddButton = showAddButton
        )

        Spacer(modifier = Modifier.height(16.dp))

        }
    }
}
