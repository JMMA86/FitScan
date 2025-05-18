package icesi.edu.co.fitscan.features.workout.ui.screens // Ajustado a tu estructura

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color // Import general Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import icesi.edu.co.fitscan.ui.theme.FitScanTheme
// Importa tus colores específicos desde Color.kt
import icesi.edu.co.fitscan.ui.theme.greenLess // Para el acento verde
import icesi.edu.co.fitscan.ui.theme.greyStrong // Para el fondo principal
// Para texto secundario, usaremos Color.Gray o Color.LightGray si no hay un color específico en tu theme
// que coincida con el B0B0B0 del screenshot. `Color.Gray` es 0xFF888888. `Color.LightGray` es 0xFFCCCCCC.
// El screenshot usa un gris claro (0xFFB0B0B0). Usaremos Color.LightGray por ahora o puedes añadir uno específico a tu Color.kt.

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDetailScreen(
    onNavigateBack: () -> Unit = {} // Callback para navegación hacia atrás
) {
    // Datos quemados para el ejercicio
    val exerciseImageUrl = "https://ibb.co/ynpkgYJK" // Link directo a la imagen
    val musclesWorked = listOf("Cuádriceps", "Isquiotibiales", "Glúteos", "Aductores")
    val exerciseName = "Sentadillas con Salto"
    val exerciseDescription = "Las sentadillas con salto son excelentes para fortalecer las piernas y mejorar la explosividad."
    val exerciseSteps = listOf(
        "Comienza de pie con los pies separados a la anchura de los hombros.",
        "Baja hasta la posición de sentadilla, manteniendo la espalda recta y las rodillas detrás de los dedos de los pies.",
        "Impulsa los talones y salta lo más alto que puedas.",
        "Aterriza suavemente y continúa con la siguiente."
    )
    val exerciseBenefit = "Este ejercicio aumenta la resistencia y fortalece la parte inferior del cuerpo."

    // Colores del tema
    val screenBackgroundColor = greyStrong
    val primaryTextColor = Color.White // Texto principal blanco
    val secondaryTextColor = Color.LightGray // Para descripciones, un gris claro (0xFFCCCCCC). Ajusta si tienes uno mejor en tu tema.
    val accentColor = greenLess // Verde de tu tema

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
                    containerColor = screenBackgroundColor, // Fondo de la TopAppBar igual al de la pantalla
                    titleContentColor = primaryTextColor,
                    navigationIconContentColor = primaryTextColor
                )
            )
        },
        // No se define bottomBar aquí, ya que es global en tu app
        containerColor = screenBackgroundColor // Fondo general del Scaffold
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Aplicar padding del Scaffold
                .verticalScroll(rememberScrollState()) // Contenido desplazable
                .background(screenBackgroundColor) // Fondo de la columna
        ) {
            // Imagen del ejercicio
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(exerciseImageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = exerciseName,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(top = 8.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Fit
            )

            // Sección Músculos trabajados
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "Musculos trabajados",
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
                        MuscleChip(text = muscle, isPrimary = muscle == "Cuádriceps", chipAccentColor = accentColor)
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

            // Nombre del ejercicio, Descripción, Pasos
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = exerciseName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = accentColor, // Título del ejercicio en color de acento
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = exerciseDescription,
                    style = MaterialTheme.typography.bodyLarge,
                    color = secondaryTextColor,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                exerciseSteps.forEachIndexed { index, step ->
                    Text(
                        text = "${index + 1}. $step",
                        style = MaterialTheme.typography.bodyMedium,
                        color = secondaryTextColor,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = exerciseBenefit,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = primaryTextColor, // Beneficio con texto primario
                    modifier = Modifier.padding(bottom = 16.dp)
                )
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