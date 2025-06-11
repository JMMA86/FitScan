package icesi.edu.co.fitscan.features.workout.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import icesi.edu.co.fitscan.ui.theme.Dimensions

@Composable
fun CurrentExerciseSection(
    name: String,
    time: String,
    series: String,
    remainingTime: String,
    repetitions: List<String>
) {
    // State for repetitions
    val repState = remember { mutableStateListOf(*repetitions.toTypedArray()) }
    LaunchedEffect(repetitions) {
        repState.clear()
        repState.addAll(repetitions)
    }

    Column(
        modifier = Modifier.padding(horizontal = Dimensions.MediumPadding)
    ) {
        Text(
            text = "Ejercicio actual",
            color = Color.Gray,
            fontSize = Dimensions.SmallTextSize,
        )
        Spacer(modifier = Modifier.height(Dimensions.SmallPadding))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(Dimensions.MediumCornerRadius))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(Dimensions.SmallPadding)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .wrapContentSize()
                        .fillMaxWidth()
                ) {
                    Text(
                        text = name,
                        fontSize = Dimensions.LargeTextSize,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = time,
                        fontSize = Dimensions.LargeTextSize,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(Dimensions.SmallPadding))
                Text(
                    text = "Serie $series",
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(Dimensions.SmallPadding))
                Text(
                    text = remainingTime,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(Dimensions.SmallPadding))
                // --- Repetitions List ---
                Box(
                    modifier = Modifier
                        .heightIn(min = 90.dp, max = 160.dp) // ~3 rows
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.FormatListNumbered, // Icon for repetition number
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Serie",
                                modifier = Modifier.weight(1f),
                                fontSize = Dimensions.MediumTextSize,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Icon(
                                imageVector = Icons.Default.Repeat, // Icon for repetitions
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Repeticiones",
                                modifier = Modifier.width(56.dp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = Dimensions.MediumTextSize
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Default.FitnessCenter, // Icon for weight
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Peso (Kg)",
                                modifier = Modifier.width(56.dp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = Dimensions.MediumTextSize
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Spacer(modifier = Modifier.width(15.dp)) // For delete button alignment
                        }
                        LazyColumn {
                            itemsIndexed(repState) { index, rep ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(rep, modifier = Modifier.weight(1f))
                                    OutlinedTextField(
                                        value = "1",
                                        onValueChange = {},
                                        modifier = Modifier
                                            .width(56.dp)
                                            .height(32.dp)
                                            .padding(horizontal = 2.dp),
                                        singleLine = true,
                                        label = { },
                                        textStyle = androidx.compose.ui.text.TextStyle.Default.copy(
                                            fontSize = Dimensions.SmallTextSize,
                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    OutlinedTextField(
                                        value = "1",
                                        onValueChange = {},
                                        modifier = Modifier
                                            .width(56.dp)
                                            .height(32.dp)
                                            .padding(horizontal = 2.dp),
                                        singleLine = true,
                                        label = { },
                                        textStyle = androidx.compose.ui.text.TextStyle.Default.copy(
                                            fontSize = Dimensions.SmallTextSize,
                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    IconButton(
                                        onClick = { repState.removeAt(index) },
                                        modifier = Modifier.size(40.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Remove repetition",
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                // --- Add Button Fixed at Bottom ---
                Button(
                    onClick = { repState.add("Rep ${repState.size + 1}") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add repetition")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Agregar repetición")
                }
            }
        }
    }
}

@Composable
@Preview
fun CurrentExerciseSectionPreview() {
    CurrentExerciseSection(
        name = "Flexiones",
        time = "30s",
        series = "1/3",
        remainingTime = "Tiempo restante: 15s",
        repetitions = listOf("Rep 1", "Rep 2", "Rep 3")
    )
}
