package icesi.edu.co.fitscan.features.workout.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun WorkoutTypeSelector(
    selectedTab: String,
    onTabSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ToggleOption(
            label = "Gym",
            selected = selectedTab == "Gym",
            onSelect = { onTabSelected("Gym") },
            modifier = Modifier.weight(1f)
        )
        ToggleOption(
            label = "Carrera",
            selected = selectedTab == "Carrera",
            onSelect = { onTabSelected("Carrera") },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun CreateWorkoutScreen() {
    var selectedTab by remember { mutableStateOf("Gym") }

    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(greyStrong)
                .verticalScroll(scrollState)
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

            // Selector de tipo de entrenamiento (Gym o Carrera)
            WorkoutTypeSelector(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )

            // El contenido específico se muestra según el tab seleccionado
            when (selectedTab) {
                "Gym" -> {
                    CreateWorkoutGymScreen()
                }
                "Carrera" -> {
                    CreateWorkoutRunningScreen()
                }
            }
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
    val bgColor = greyMed
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
fun SuggestionChip(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = greyMed,
            contentColor = Color.White
        ),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text)
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_add),
            contentDescription = "Agregar ejercicio",
            modifier = Modifier.size(18.dp),
            tint = greenLess
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CreateWorkoutScreenPreview() {
    FitScanTheme {
        CreateWorkoutScreen()
    }
}