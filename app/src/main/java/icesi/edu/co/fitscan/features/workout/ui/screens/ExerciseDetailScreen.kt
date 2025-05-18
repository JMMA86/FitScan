package icesi.edu.co.fitscan.features.workout.ui.screens // Ajustado a tu estructura

import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import icesi.edu.co.fitscan.features.workout.ui.viewmodel.ExerciseDetailState
import icesi.edu.co.fitscan.features.workout.ui.viewmodel.ExerciseDetailViewModel
import icesi.edu.co.fitscan.ui.theme.FitScanTheme
import icesi.edu.co.fitscan.ui.theme.greenLess
import icesi.edu.co.fitscan.ui.theme.greyStrong

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDetailScreen(
    exerciseId: String = "ID_EJERCICIO_AQUI", // Hardcodea un ID para pruebas
    onNavigateBack: () -> Unit = {}
) {
    val viewModel: ExerciseDetailViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(exerciseId) {
        viewModel.loadExercise(exerciseId)
    }

    // Colores del tema
    val screenBackgroundColor = greyStrong
    val primaryTextColor = Color.White
    val secondaryTextColor = Color.LightGray
    val accentColor = greenLess

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del ejercicio", color = primaryTextColor) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = primaryTextColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = screenBackgroundColor,
                    titleContentColor = primaryTextColor,
                    navigationIconContentColor = primaryTextColor
                )
            )
        },
        // No se define bottomBar aquí, ya que es global en tu app
        containerColor = screenBackgroundColor // Fondo general del Scaffold
    ) { innerPadding ->
        when (state) {
            is ExerciseDetailState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = accentColor)
                }
            }
            is ExerciseDetailState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text((state as ExerciseDetailState.Error).message, color = Color.Red)
                }
            }
            is ExerciseDetailState.Success -> {
                val exercise = (state as ExerciseDetailState.Success).exercise
                val exerciseName = exercise.name ?: "-"
                val exerciseDescription = exercise.description ?: "-"
                val musclesWorked = exercise.muscleGroups?.split(",")?.map { it.trim() } ?: emptyList()
                // Aquí puedes agregar más campos si tu modelo los tiene

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                        .background(screenBackgroundColor)
                ) {
                    // Aquí puedes mostrar la imagen si tienes una URL en el modelo
                    // AsyncImage(...)

                    // Sección Músculos trabajados
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Text(
                            text = "Músculos trabajados",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = primaryTextColor,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            musclesWorked.take(2).forEach { muscle ->
                                MuscleChip(text = muscle, isPrimary = muscle == musclesWorked.firstOrNull(), chipAccentColor = accentColor)
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            musclesWorked.drop(2).forEach { muscle ->
                                MuscleChip(text = muscle, chipAccentColor = accentColor)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    // Nombre, descripción, pasos (si los tienes en el modelo)
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Text(
                            text = exerciseName,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = accentColor,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = exerciseDescription,
                            style = MaterialTheme.typography.bodyLarge,
                            color = secondaryTextColor,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        // Si tienes pasos o beneficios, agrégalos aquí
                    }
                }
            }
        }
    }
}

@Composable
fun MuscleChip(text: String, isPrimary: Boolean = false, chipAccentColor: Color) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, chipAccentColor),
        color = Color.Transparent,
        contentColor = chipAccentColor // Color del texto y el ícono dentro del chip
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            if (isPrimary) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Músculo primario",
                    tint = chipAccentColor,
                    modifier = Modifier.size(16.dp).padding(end = 4.dp)
                )
            }
            Text(
                text = text,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
                // El color del texto se hereda de contentColor de Surface
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExerciseDetailScreenPreview() {
    FitScanTheme { // Asegúrate que FitScanTheme configure correctamente los colores base
        ExerciseDetailScreen()
    }
}