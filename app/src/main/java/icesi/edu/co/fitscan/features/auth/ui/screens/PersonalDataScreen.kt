package icesi.edu.co.fitscan.features.auth.ui.screens

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import icesi.edu.co.fitscan.R
import icesi.edu.co.fitscan.features.auth.ui.model.BodyMeasureUiState
import icesi.edu.co.fitscan.features.auth.ui.viewmodel.BodyMeasurementViewModel
import icesi.edu.co.fitscan.ui.theme.FitScanTheme

@Composable
fun PersonalDataScreen(
    greenLess: Color = MaterialTheme.colorScheme.primary,
    bodyMeasurementViewModel: BodyMeasurementViewModel = viewModel(),
    onMeasurementsComplete: () -> Unit = {}
) {
    val context = LocalContext.current
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }

    var arms by remember { mutableStateOf("") }
    var chest by remember { mutableStateOf("") }
    var waist by remember { mutableStateOf("") }
    var hips by remember { mutableStateOf("") }
    var thighs by remember { mutableStateOf("") }
    var calves by remember { mutableStateOf("") }

    var allergies by remember { mutableStateOf(TextFieldValue("")) }

    var trainingLevel by remember { mutableStateOf("") }
    var mainGoal by remember { mutableStateOf("") }

    val trainingLevels = listOf("Principiante", "Intermedio", "Avanzado")
    val mainGoals = listOf("Bajar de peso", "Ganar músculo", "Mantenerse en forma")

    var expandedLevel by remember { mutableStateOf(false) }
    var expandedGoal by remember { mutableStateOf(false) }

    val uiState by bodyMeasurementViewModel.uiState.collectAsState()
    val isEstimating by bodyMeasurementViewModel.isEstimating.collectAsState()
    val estimatedMeasurements by bodyMeasurementViewModel.estimatedMeasurements.collectAsState()    // Check camera availability for emulator compatibility
    val isCameraAvailable = remember {
        context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
    }

    // Camera launcher for taking photos with emulator support
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            try {
                val tempUri = saveBitmapToTempFile(context, bitmap)
                tempUri?.let { uri ->
                    val existingMeasurements = mapOf(
                        "arms" to (arms.toDoubleOrNull() ?: 0.0),
                        "chest" to (chest.toDoubleOrNull() ?: 0.0),
                        "waist" to (waist.toDoubleOrNull() ?: 0.0),
                        "hips" to (hips.toDoubleOrNull() ?: 0.0),
                        "thighs" to (thighs.toDoubleOrNull() ?: 0.0),
                        "calves" to (calves.toDoubleOrNull() ?: 0.0)
                    )
                    
                    bodyMeasurementViewModel.estimateMeasurementsFromImage(
                        imageUri = uri,
                        height = height.toDoubleOrNull() ?: 0.0,
                        weight = weight.toDoubleOrNull() ?: 0.0,
                        existingMeasurements = existingMeasurements
                    )
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error procesando imagen: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "No se pudo capturar la imagen", Toast.LENGTH_SHORT).show()
        }
    }

    // Update fields when AI estimation completes
    LaunchedEffect(estimatedMeasurements) {
        estimatedMeasurements?.let { measurements ->
            measurements.arms_cm?.let { if (arms.isEmpty()) arms = it.toString() }
            measurements.chest_cm?.let { if (chest.isEmpty()) chest = it.toString() }
            measurements.waist_cm?.let { if (waist.isEmpty()) waist = it.toString() }
            measurements.hips_cm?.let { if (hips.isEmpty()) hips = it.toString() }
            measurements.thighs_cm?.let { if (thighs.isEmpty()) thighs = it.toString() }
            measurements.calves_cm?.let { if (calves.isEmpty()) calves = it.toString() }        }
    }
      LaunchedEffect(uiState) {
        val currentState = uiState
        when (currentState) {
            is BodyMeasureUiState.Success -> {
                onMeasurementsComplete()
                bodyMeasurementViewModel.resetState()
            }

            is BodyMeasureUiState.Error -> {
                Toast.makeText(context, currentState.message, Toast.LENGTH_LONG).show()
            }

            else -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.img_login),
            contentDescription = "Fondo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Datos Personales",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                modifier = Modifier.padding(top = 40.dp, bottom = 6.dp)
            )

            // Basic Information content
            Column {
                SectionTitle("Información Básica")
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    FitInput(
                        "Altura",
                        "cm",
                        height,
                        { height = it },
                        Modifier.weight(1f),
                        greenLess
                    )
                    FitInput("Peso", "kg", weight, { weight = it }, Modifier.weight(1f), greenLess)
                }

                Spacer(Modifier.height(18.dp))

                SectionTitle("Medidas Corporales")
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        FitInput(
                            "Brazos",
                            "cm",
                            arms,
                            { arms = it },
                            Modifier.fillMaxWidth(),
                            greenLess
                        )
                        FitInput(
                            "Cintura",
                            "cm",
                            waist,
                            { waist = it },
                            Modifier.fillMaxWidth(),
                            greenLess
                        )
                        FitInput(
                            "Muslos",
                            "cm",
                            thighs,
                            { thighs = it },
                            Modifier.fillMaxWidth(),
                            greenLess
                        )
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {                        FitInput(
                            "Pecho",
                            "cm",
                            chest,
                            { chest = it },
                            Modifier.fillMaxWidth(),
                            greenLess
                        )
                        FitInput(
                            "Caderas",
                            "cm",
                            hips,
                            { hips = it },
                            Modifier.fillMaxWidth(),
                            greenLess
                        )
                        FitInput(
                            "Pantorrilla",
                            "cm",
                            calves,
                            { calves = it },
                            Modifier.fillMaxWidth(),
                            greenLess
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))                // AI Body Measurements Scan Button
                OutlinedButton(
                    onClick = {
                        when {
                            !isCameraAvailable -> {
                                Toast.makeText(context, "Cámara no disponible en este dispositivo", Toast.LENGTH_SHORT).show()
                            }
                            height.isEmpty() || weight.isEmpty() -> {
                                Toast.makeText(context, "Complete altura y peso primero", Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                try {
                                    takePictureLauncher.launch(null)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Error al acceder a la cámara: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isCameraAvailable && height.isNotEmpty() && weight.isNotEmpty() && !isEstimating,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = if (isCameraAvailable) greenLess else Color.Gray
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    if (isEstimating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = greenLess
                        )
                        Spacer(Modifier.padding(4.dp))
                        Text("Analizando...")
                    } else {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Camera",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.padding(4.dp))
                        Text(if (isCameraAvailable) "Escanear Medidas Corporales" else "Cámara no disponible")
                    }
                }

                // Status messages
                when {
                    !isCameraAvailable -> {
                        Text(
                            text = "Función de cámara no disponible en este dispositivo",
                            color = Color.Red.copy(alpha = 0.8f),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    height.isEmpty() || weight.isEmpty() -> {
                        Text(
                            text = "Complete altura y peso para usar el escáner AI",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    else -> {
                        Text(
                            text = "Tome una foto para estimar medidas corporales automáticamente",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                Spacer(Modifier.height(18.dp))

                SectionTitle("Información de salud")
                Text(
                    text = "Alergias",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                OutlinedTextField(
                    value = allergies,
                    onValueChange = { allergies = it },
                    placeholder = { Text("Ej: Maní, mariscos...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = greenLess,
                        unfocusedBorderColor = greenLess,
                        focusedPlaceholderColor = greenLess,
                        unfocusedPlaceholderColor = greenLess,
                    )
                )

                Spacer(Modifier.height(18.dp))

                SectionTitle("Perfil de entrenamiento")
                FitDropdown("Nivel de entrenamiento", trainingLevels, trainingLevel, {
                    trainingLevel = it
                    expandedLevel = false
                }, expandedLevel, { expandedLevel = it }, greenLess)

                Spacer(Modifier.height(18.dp))

                FitDropdown("Objetivo principal", mainGoals, mainGoal, {
                    mainGoal = it
                    expandedGoal = false
                }, expandedGoal, { expandedGoal = it }, greenLess)
            }

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    bodyMeasurementViewModel.saveMeasurements(
                        height = height.toDoubleOrNull() ?: 0.0,
                        weight = weight.toDoubleOrNull() ?: 0.0,
                        arms = arms.toDoubleOrNull() ?: 0.0,
                        chest = chest.toDoubleOrNull() ?: 0.0,
                        waist = waist.toDoubleOrNull() ?: 0.0,
                        hips = hips.toDoubleOrNull() ?: 0.0,
                        thighs = thighs.toDoubleOrNull() ?: 0.0,
                        calves = calves.toDoubleOrNull() ?: 0.0
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 40.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = greenLess),
                shape = RoundedCornerShape(8.dp)
            ) {
                if (uiState is BodyMeasureUiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                } else {
                    Text("Completar perfil", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun FitInput(
    title: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier,
    greenLess: Color
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = {
                if (it.all { char -> char.isDigit() }) onValueChange(it)
            },
            placeholder = { Text(placeholder) },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = greenLess,
                unfocusedBorderColor = greenLess,
                focusedPlaceholderColor = greenLess,
                unfocusedPlaceholderColor = greenLess,
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitDropdown(
    label: String,
    options: List<String>,
    selectedOption: String,
    onSelect: (String) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    greenLess: Color
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = greenLess,
                unfocusedBorderColor = greenLess,
                focusedLabelColor = greenLess,
                unfocusedLabelColor = greenLess,
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        onSelect(selectionOption)
                    }
                )
            }
        }
    }
}

// Helper function to save bitmap to temporary file
private fun saveBitmapToTempFile(context: android.content.Context, bitmap: android.graphics.Bitmap): Uri? {
    return try {
        val file = java.io.File(context.cacheDir, "temp_body_photo_${System.currentTimeMillis()}.jpg")
        val outputStream = java.io.FileOutputStream(file)
        bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 85, outputStream)
        outputStream.flush()
        outputStream.close()
        androidx.core.content.FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    } catch (e: Exception) {
        null
    }
}

@Preview(showBackground = true)
@Composable
fun PersonalDataScreenPreview() {
    FitScanTheme {
        //PersonalDataScreen(Color(0xFF4CAF50), BodyMeasurementViewModel(), {})
    }
}