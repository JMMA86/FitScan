package icesi.edu.co.fitscan.features.workout.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import icesi.edu.co.fitscan.R
import icesi.edu.co.fitscan.features.common.ui.components.ToggleOption
import icesi.edu.co.fitscan.ui.theme.FitScanTheme
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

@Preview(showBackground = true)
@Composable
fun CreateWorkoutScreenPreview() {
    FitScanTheme {
        CreateWorkoutScreen()
    }
}