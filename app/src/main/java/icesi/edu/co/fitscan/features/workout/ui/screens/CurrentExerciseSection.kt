package icesi.edu.co.fitscan.features.workout.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import icesi.edu.co.fitscan.ui.theme.Dimensions

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    textStyle: androidx.compose.ui.text.TextStyle = androidx.compose.ui.text.TextStyle()
) {
    Box(
        modifier = modifier
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline,
                RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 8.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = keyboardOptions,
            textStyle = textStyle.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun CurrentExerciseSection(
    name: String,
    time: String,
    series: String,
    remainingTime: String,
    repetitions: List<String>,
    initialRepsValues: List<Int> = emptyList(),
    initialKilosValues: List<Int> = emptyList(),
    onRepsChanged: (List<Int>) -> Unit = {},
    onKilosChanged: (List<Int>) -> Unit = {},
    onSetsCountChanged: (Int) -> Unit = {},
    isTimeExceeded: Boolean = false,
    onExerciseDetailClick: () -> Unit = {}
) {
    // State for repetitions
    val repState = remember { mutableStateListOf(*repetitions.toTypedArray()) }

    // Evitamos ciclos de recomposición y sobrecarga de logs
    val isInitializing = remember { mutableStateOf(true) }

    // Convertir valores numéricos a string para mostrar en TextFields
    var repsValues by remember(name) {
        mutableStateOf(
            if (initialRepsValues.isNotEmpty()) {
                Log.d(
                    "CurrentExerciseSection",
                    "Inicializando repsValues para ejercicio: $name con valores: $initialRepsValues"
                )
                initialRepsValues.map { it.toString() }
            } else {
                Log.d(
                    "CurrentExerciseSection",
                    "Inicializando repsValues con valores por defecto para ejercicio: $name"
                )
                List(repetitions.size) { "1" }
            }
        )
    }

    var kilosValues by remember(name) {
        mutableStateOf(
            if (initialKilosValues.isNotEmpty()) {
                Log.d(
                    "CurrentExerciseSection",
                    "Inicializando kilosValues para ejercicio: $name con valores: $initialKilosValues"
                )
                initialKilosValues.map { it.toString() }
            } else {
                Log.d(
                    "CurrentExerciseSection",
                    "Inicializando kilosValues con valores por defecto para ejercicio: $name"
                )
                List(repetitions.size) { "1" }
            }
        )
    }

    // Sincronización cuando cambian las repeticiones o el ejercicio
    LaunchedEffect(key1 = name, key2 = repetitions) {
        repState.clear()
        repState.addAll(repetitions)

        // Reset initialization flag when exercise changes
        isInitializing.value = true

        // Update values with initial values if available, preserving existing values and padding as needed
        if (initialRepsValues.isNotEmpty()) {
            // Use stored values and pad with defaults if the list is shorter than repetitions
            val newRepsValues = initialRepsValues.map { it.toString() }.toMutableList()
            while (newRepsValues.size < repetitions.size) {
                newRepsValues.add("1")
            }
            repsValues = newRepsValues.take(repetitions.size)
            Log.d(
                "CurrentExerciseSection",
                "Usando valores guardados de reps para $name: $repsValues"
            )
        } else {
            repsValues = List(repetitions.size) { "1" }
            Log.d(
                "CurrentExerciseSection",
                "Usando valores por defecto de reps para $name: $repsValues"
            )
        }

        if (initialKilosValues.isNotEmpty()) {
            // Use stored values and pad with defaults if the list is shorter than repetitions
            val newKilosValues = initialKilosValues.map { it.toString() }.toMutableList()
            while (newKilosValues.size < repetitions.size) {
                newKilosValues.add("1")
            }
            kilosValues = newKilosValues.take(repetitions.size)
            Log.d(
                "CurrentExerciseSection",
                "Usando valores guardados de kilos para $name: $kilosValues"
            )
        } else {
            kilosValues = List(repetitions.size) { "1" }
            Log.d(
                "CurrentExerciseSection",
                "Usando valores por defecto de kilos para $name: $kilosValues"
            )
        }

        // Allow updates after a short delay
        kotlinx.coroutines.delay(100)
        isInitializing.value = false
    }

    // Sincronización cuando cambia el tamaño de repState (por ejemplo, al agregar/eliminar sets)
    LaunchedEffect(repState.size) {
        // Si aumentó el tamaño, agregar nuevos valores por defecto
        if (repsValues.size < repState.size) {
            val newRepsValues = repsValues.toMutableList()
            // Agregar valores por defecto solo para los nuevos elementos
            for (i in repsValues.size until repState.size) {
                newRepsValues.add("1")
            }
            repsValues = newRepsValues
        }
        // Si disminuyó, reducir la lista
        else if (repsValues.size > repState.size) {
            repsValues = repsValues.take(repState.size)
        }

        // Hacer lo mismo con kilosValues
        if (kilosValues.size < repState.size) {
            val newKilosValues = kilosValues.toMutableList()
            for (i in kilosValues.size until repState.size) {
                newKilosValues.add("1")
            }
            kilosValues = newKilosValues
        } else if (kilosValues.size > repState.size) {
            kilosValues = kilosValues.take(repState.size)
        }

        // Solo enviamos los valores al ViewModel si ya terminamos la inicialización
        if (!isInitializing.value) {
            onRepsChanged(repsValues.map { it.toIntOrNull() ?: 1 })
            onKilosChanged(kilosValues.map { it.toIntOrNull() ?: 1 })
            // Notificar el cambio en el número de series
            onSetsCountChanged(repState.size)
            Log.d("CurrentExerciseSection", "Notificando cambio de sets count a: ${repState.size}")
        }
    }

    // Efecto para enviar cambios al viewModel solo cuando el usuario modifique los valores manualmente
    LaunchedEffect(repsValues) {
        if (!isInitializing.value) {
            Log.d(
                "CurrentExerciseSection",
                "Usuario modificó repeticiones para ejercicio $name: $repsValues"
            )
            onRepsChanged(repsValues.map { it.toIntOrNull() ?: 1 })
        }
    }

    LaunchedEffect(kilosValues) {
        if (!isInitializing.value) {
            Log.d(
                "CurrentExerciseSection",
                "Usuario modificó pesos para ejercicio $name: $kilosValues"
            )
            onKilosChanged(kilosValues.map { it.toIntOrNull() ?: 1 })
        }
    }

    // --- UI CODE ---
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
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = name,
                        fontSize = Dimensions.LargeTextSize,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = onExerciseDetailClick,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = "Ver detalles del ejercicio",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
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
                    text = remainingTime,
                    color = if (isTimeExceeded) Color.Red else MaterialTheme.colorScheme.onSurface,
                    fontWeight = if (isTimeExceeded) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Normal
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
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            itemsIndexed(repState) { index, rep ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(rep, modifier = Modifier.weight(1f))
                                    CustomTextField(
                                        value = repsValues.getOrNull(index) ?: "",
                                        onValueChange = { newValue ->
                                            if (newValue.all { it.isDigit() } || newValue.isEmpty()) {
                                                val value =
                                                    if (newValue.isEmpty()) "0" else newValue
                                                val updatedList = repsValues.toMutableList()
                                                updatedList[index] = value
                                                repsValues = updatedList
                                                Log.d(
                                                    "CurrentExerciseSection",
                                                    "Rep $index changed to: $value"
                                                )
                                            }
                                        },
                                        modifier = Modifier
                                            .width(56.dp)
                                            .height(40.dp),
                                        textStyle = androidx.compose.ui.text.TextStyle(
                                            fontSize = Dimensions.MediumTextSize,
                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                                        ),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    CustomTextField(
                                        value = kilosValues.getOrNull(index) ?: "",
                                        onValueChange = { newValue ->
                                            if (newValue.all { it.isDigit() } || newValue.isEmpty()) {
                                                val value =
                                                    if (newValue.isEmpty()) "0" else newValue
                                                // Actualizar solo el elemento específico
                                                val updatedList = kilosValues.toMutableList()
                                                updatedList[index] = value
                                                kilosValues = updatedList
                                                Log.d(
                                                    "CurrentExerciseSection",
                                                    "Kilo $index changed to: $value"
                                                )
                                            }
                                        },
                                        modifier = Modifier
                                            .width(56.dp)
                                            .height(40.dp),
                                        textStyle = androidx.compose.ui.text.TextStyle(
                                            fontSize = Dimensions.MediumTextSize,
                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                                        ),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
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
                    onClick = {
                        repState.add("Rep ${repState.size + 1}")
                        Log.d(
                            "CurrentExerciseSection",
                            "Agregada nueva repetición. repState ahora tiene ${repState.size} elementos"
                        )
                    },
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
        repetitions = listOf("Rep 1", "Rep 2", "Rep 3"),
        onSetsCountChanged = {},
        isTimeExceeded = false
    )
}
