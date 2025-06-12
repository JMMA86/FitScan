package icesi.edu.co.fitscan.features.workout.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import icesi.edu.co.fitscan.R
import icesi.edu.co.fitscan.features.workout.ui.components.ExerciseImageThumbnail

@Composable
fun ExerciseCard(
    name: String,
    sets: Int,
    reps: Int,
    onSetsChange: (Int) -> Unit,
    onRepsChange: (Int) -> Unit,
    onRemove: () -> Unit,
    onAdd: () -> Unit,
    showAddButton: Boolean,
) {    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Exercise thumbnail
        ExerciseImageThumbnail(
            exerciseName = name,
            size = 40,
            isCircular = true
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Text(
            text = name,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            var setsText by remember(sets) { mutableStateOf(sets.toString()) }
            var repsText by remember(reps) { mutableStateOf(reps.toString()) }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("SETS", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                Box(
                    modifier = Modifier.width(40.dp),
                    contentAlignment = Alignment.Center
                ) {                    BasicTextField(
                        value = setsText,
                        onValueChange = {
                            if (it.isEmpty()) {
                                setsText = it
                            } else if (it.all { c -> c.isDigit() } && it.length <= 2) {
                                val value = it.toIntOrNull() ?: 0
                                if (value in 1..99) {
                                    setsText = it
                                    onSetsChange(value)
                                }
                            }
                        },
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        ),
                        singleLine = true
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("REPS", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                Box(
                    modifier = Modifier.width(40.dp),
                    contentAlignment = Alignment.Center
                ) {                    BasicTextField(
                        value = repsText,
                        onValueChange = {
                            if (it.isEmpty()) {
                                repsText = it
                            } else if (it.all { c -> c.isDigit() } && it.length <= 2) {
                                val value = it.toIntOrNull() ?: 0
                                if (value in 1..99) {
                                    repsText = it
                                    onRepsChange(value)
                                }
                            }
                        },
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        ),
                        singleLine = true
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            if (showAddButton) {
                IconButton(
                    onClick = onAdd,
                    modifier = Modifier
                        .size(30.dp)
                        .background(Color.Transparent)
                ) {                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = "Añadir",
                        tint = MaterialTheme.colorScheme.primary
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
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}