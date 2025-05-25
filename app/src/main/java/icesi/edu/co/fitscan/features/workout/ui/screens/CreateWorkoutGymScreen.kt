package icesi.edu.co.fitscan.features.workout.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import icesi.edu.co.fitscan.R
import icesi.edu.co.fitscan.features.auth.ui.viewmodel.BodyMeasurementViewModel
import icesi.edu.co.fitscan.features.auth.ui.model.BodyMeasureUiState
import icesi.edu.co.fitscan.features.common.ui.components.FitScanTextField
import icesi.edu.co.fitscan.features.common.ui.components.SectionTitle
import icesi.edu.co.fitscan.features.common.ui.components.SuggestionChip
import icesi.edu.co.fitscan.features.common.ui.viewmodel.AppState
import icesi.edu.co.fitscan.features.openai.data.usecases.GetChatCompletionUseCaseImpl
import icesi.edu.co.fitscan.features.workout.ui.components.CreateWorkoutButton
import icesi.edu.co.fitscan.features.workout.ui.components.ExerciseList
import icesi.edu.co.fitscan.features.workout.ui.viewmodel.CreateWorkoutGymViewModel
import icesi.edu.co.fitscan.features.workout.ui.viewmodel.factory.CreateWorkoutGymViewModelFactory
import icesi.edu.co.fitscan.ui.theme.FitScanTheme
import icesi.edu.co.fitscan.ui.theme.greyStrong
import kotlinx.coroutines.launch
import org.json.JSONObject

@Composable
fun CreateWorkoutGymScreen() {
    val viewModel: CreateWorkoutGymViewModel = viewModel(factory = CreateWorkoutGymViewModelFactory())
    val exercises by viewModel.exercises.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val saveSuccess by viewModel.saveSuccess.collectAsState()
    val saveError by viewModel.saveError.collectAsState()

    val bodyMeasurementViewModel: BodyMeasurementViewModel = viewModel()
    val bodyMeasureState by bodyMeasurementViewModel.uiState.collectAsState()
    val userId = AppState.customerId
    var bodyMeasureId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(userId) {
        if (userId != null) {
            bodyMeasurementViewModel.getBodyMeasurementsByCustomerId(userId)
        }
    }

    LaunchedEffect(bodyMeasureState) {
        if (bodyMeasureState is BodyMeasureUiState.SuccessData) {
            val data = (bodyMeasureState as BodyMeasureUiState.SuccessData).data
            bodyMeasureId = data.id
        }
    }

    val bodyMeasures = when (bodyMeasureState) {
        is BodyMeasureUiState.SuccessData -> (bodyMeasureState as BodyMeasureUiState.SuccessData).data
        else -> null
    }

    val context = LocalContext.current
    val chatCompletionUseCase = remember { GetChatCompletionUseCaseImpl(context) }
    var openAiResponse by remember { mutableStateOf("Cargando respuesta...") }
    val scope = rememberCoroutineScope()

    val scrollState = rememberScrollState()
    var workoutName by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }

    // Diálogo de confirmación o error
    if (saveSuccess != null || saveError != null) {
        Dialog(onDismissRequest = {
            if (saveSuccess != null) {
                // Si fue exitoso, reiniciar la pantalla
                workoutName = ""
                viewModel.reload()
            } else {
                // Si hubo error, solo cerrar el diálogo
                viewModel.resetSaveState()
            }
        }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (saveSuccess != null) "¡Entrenamiento creado!" else "Error",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = saveSuccess?.let { "El entrenamiento '${it.name}' ha sido guardado correctamente." }
                            ?: saveError ?: "Ha ocurrido un error desconocido",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (saveSuccess != null) {
                                // Si fue exitoso, reiniciar la pantalla
                                workoutName = ""
                                viewModel.reload()
                            } else {
                                // Si hubo error, solo cerrar el diálogo
                                viewModel.resetSaveState()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Aceptar")
                    }
                }
            }
        }
    }

    // Función para calcular la altura dinámica basada en el número de elementos
    fun calculateDynamicHeight(itemCount: Int): Dp {
        // Altura por tarjeta (aproximada) y altura mínima y máxima
        val cardHeight = 70.dp
        val minHeight = 0.dp
        val maxHeight = 350.dp

        // Calcular altura basada en el número de elementos (y espacio entre ellos)
        val calculatedHeight = (cardHeight * itemCount) + if (itemCount > 1) (16.dp * (itemCount - 1)) else 0.dp

        // Asegurar que la altura esté dentro de los límites
        return when {
            calculatedHeight < minHeight -> minHeight
            calculatedHeight > maxHeight -> maxHeight
            else -> calculatedHeight
        }
    }

    // Convertir los ejercicios del dominio a nombres para la interfaz
    val availableExercises = remember(exercises) {
        mutableStateListOf<String>().apply {
            clear()
            addAll(exercises.map { it.name })
        }
    }

    val filteredAvailableExercises = availableExercises.filter {
        it.contains(searchQuery, ignoreCase = true)
    }

    val addedExercises = remember { mutableStateListOf<String>() }

    // Llamado para obtener las medidas del usuario actual
    LaunchedEffect(Unit) {
        scope.launch{
            chatCompletionUseCase(
                "De acuerdo a las siguientes medidas: " + bodyMeasures.toString() + " " +
                        "recomiendame ejercicios que sirvan para hacer de esta persona una con buen estado físico " +
                        "y que estén en un formato de lista. Tu respuesta debe ser únicamente una lista de máximo 10 ejercicios " +
                        "sin decirme 'Aqui tienes...' ni nada por el estilo, únicamente la lista como por ejemplo:" +
                        "'Press de banca', 'Sentadillas', 'Deadlift', 'Push ups'... Dámelo en formato JSON, donde solo sea un arreglo " +
                        "con el parametro 'names'. La respuesta debe ir sin '```' ni la palabra 'json'"
            ).fold(
                onSuccess = { response ->
                    openAiResponse = response
                },
                onFailure = { error ->
                    openAiResponse = "Error: ${error.message}"
                }
            )
        }
    }

    // Extraer lista de ejercicios
    fun parseSuggestionsFromJson(json: String): List<String> {
        return try {
            val jsonObject = JSONObject(json)
            val namesArray = jsonObject.getJSONArray("names")
            List(namesArray.length()) { i -> namesArray.getString(i) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    val allSuggestions = remember(openAiResponse) {
        parseSuggestionsFromJson(openAiResponse).ifEmpty {
            listOf("Press de banca", "Sentadillas", "Deadlift", "Push ups")
        }
    }
    val suggestedExercises = remember { mutableStateListOf(*allSuggestions.toTypedArray()) }

    val exerciseData = remember { mutableStateMapOf<String, Pair<Int, Int>>() }

    // Actualizar exerciseData cuando se cargan nuevos ejercicios
    LaunchedEffect(exercises) {
        exercises.forEach { exercise ->
            if (!exerciseData.containsKey(exercise.name)) {
                exerciseData[exercise.name] = Pair(4, 10)
            }
        }

        allSuggestions.forEach { suggestion ->
            if (!exerciseData.containsKey(suggestion)) {
                exerciseData[suggestion] = Pair(4, 10)
            }
        }
    }

    // Actualizar sugerencias cuando llega la respuesta de GPT
    LaunchedEffect(openAiResponse) {
        val parsed = parseSuggestionsFromJson(openAiResponse)
        suggestedExercises.clear()
        if (parsed.isNotEmpty()) {
            suggestedExercises.addAll(parsed)
        } else {
            suggestedExercises.addAll(listOf("Press de banca", "Sentadillas", "Deadlift", "Push ups"))
        }
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

            // Box con altura calculada dinámicamente basada en el número de ejercicios
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (filteredAvailableExercises.isEmpty()) 75.dp else calculateDynamicHeight(filteredAvailableExercises.size))
            ) {
                if (isLoading) {
                    // Mostrar animación de carga
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                } else if (filteredAvailableExercises.isEmpty()) {
                    // Mostrar mensaje cuando no hay ejercicios o no coinciden con la búsqueda
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (searchQuery.isNotEmpty())
                                "Ningún ejercicio coincide con el criterio de búsqueda"
                            else "No hay más ejercicios disponibles",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                } else {
                    // Mostrar la lista de ejercicios
                    ExerciseList(
                        exercises = filteredAvailableExercises,
                        exerciseData = exerciseData,
                        showAddButton = true,
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
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
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

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

            // Box con altura calculada dinámicamente basada en el número de ejercicios añadidos
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (addedExercises.isEmpty()) 70.dp else calculateDynamicHeight(addedExercises.size))
            ) {
                if (addedExercises.isEmpty()) {
                    // Mostrar mensaje cuando no hay ejercicios añadidos
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No se ha agregado ningún ejercicio",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                } else {
                    ExerciseList(
                        exercises = addedExercises,
                        exerciseData = exerciseData,
                        showAddButton = false,
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
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
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Spacer(modifier = Modifier.weight(1f))

            Box(modifier = Modifier.padding(bottom = 16.dp)) {
                CreateWorkoutButton(
                    onClick = {
                        val exercisesToSave = addedExercises.map { name ->
                            name to (exerciseData[name] ?: Pair(4, 10))
                        }
                        viewModel.createWorkout(workoutName, exercisesToSave)
                    },
                    icon = R.drawable.ic_fitness,
                    text = "Guardar entrenamiento",
                    enabled = !isSaving && workoutName.isNotBlank() && addedExercises.isNotEmpty(),
                    isLoading = isSaving
                )
            }
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
