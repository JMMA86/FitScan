package icesi.edu.co.fitscan.features.statistics.ui.screens

import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import icesi.edu.co.fitscan.features.common.ui.components.FitScanHeader
import icesi.edu.co.fitscan.features.statistics.ui.viewmodel.ProgressPhotoViewModel
import icesi.edu.co.fitscan.features.statistics.ui.viewmodel.factory.ProgressPhotoViewModelFactory

@Composable
fun ProgressPhotoScreen(navController: NavController = rememberNavController()) {
    val viewModel: ProgressPhotoViewModel = viewModel(factory = ProgressPhotoViewModelFactory())
    val context = LocalContext.current
    val progressPhotos by viewModel.progressPhotos.collectAsState()
    var title by remember { mutableStateOf("Inserta un título") }
    var isEditingTitle by remember { mutableStateOf(false) }
    var viewMode by remember { mutableStateOf("grid") }
    var sortBy by remember { mutableStateOf("date_desc") }
    var selectedPhoto by remember {
        mutableStateOf<icesi.edu.co.fitscan.domain.model.ProgressPhoto?>(null)
    }
    var editingPhotoTitle by remember { mutableStateOf("") }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var photoToDelete by remember {
        mutableStateOf<icesi.edu.co.fitscan.domain.model.ProgressPhoto?>(null)
    }
    var isInitialLoad by remember { mutableStateOf(true) }

    LaunchedEffect(progressPhotos) {
        if (progressPhotos.isNotEmpty() && !isEditingTitle && isInitialLoad) {
            title = progressPhotos.first().title ?: "Inserta un título"
            isInitialLoad = false
        }
    }

    val pickImageLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
                uri: Uri? ->
            uri?.let {
                // Always use default title for new photos
                viewModel.uploadProgressPhoto(context, it, "Inserta un título")
            }
        }

    val takePictureLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicturePreview()
        ) { bitmap: Bitmap? ->
            if (bitmap != null) {
                try {
                    val tempUri = saveBitmapToTempFile(context, bitmap)
                    tempUri?.let { uri ->
                        // Always use default title for new photos
                        viewModel.uploadProgressPhoto(context, uri, "Inserta un título")
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Error procesando imagen: ${e.message}",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            } else {
                Toast.makeText(context, "No se pudo capturar la imagen", Toast.LENGTH_SHORT).show()
            }
        }

    val sortedPhotos =
        remember(progressPhotos, sortBy) {
            when (sortBy) {
                "date_asc" -> progressPhotos.sortedBy { it.photoDate }
                "name_asc" -> progressPhotos.sortedBy { it.title ?: "" }
                "name_desc" -> progressPhotos.sortedByDescending { it.title ?: "" }
                else -> progressPhotos.sortedByDescending { it.photoDate }            }
        }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            FitScanHeader(
                title = "Toma una foto de las gains 🔥🔥🔥",
                navController = navController
            )

            if (progressPhotos.isNotEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().aspectRatio(1.2f)) {
                    SubcomposeAsyncImage(
                        model =
                            ImageRequest.Builder(LocalContext.current)
                                .data(
                                    "https://fitscan.onrender.com/assets/${progressPhotos.first().imagePath}"
                                )
                                .crossfade(true)
                                .build(),
                        contentDescription = "Última foto de progreso",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        loading = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        },
                        error = {
                            Box(
                                modifier =
                                    Modifier.fillMaxSize()
                                        .background(MaterialTheme.colorScheme.surfaceVariant),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Error cargando imagen",
                                    color = MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    )
                    Box(
                        modifier =
                            Modifier.fillMaxSize()
                                .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.4f))
                    ) {
                        // Title section centered both vertically and horizontally
                        Column(
                            modifier = Modifier.align(Alignment.Center).padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (isEditingTitle) {
                                OutlinedTextField(
                                    value = title,
                                    onValueChange = { title = it },
                                    singleLine = true,
                                    modifier = Modifier.padding(16.dp),
                                    textStyle =
                                        LocalTextStyle.current.copy(
                                            color = MaterialTheme.colorScheme.onSurface
                                        ),
                                    colors =
                                        OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor =
                                                MaterialTheme.colorScheme.onSurface,
                                            unfocusedBorderColor =
                                                MaterialTheme.colorScheme.onSurface,
                                            cursorColor = MaterialTheme.colorScheme.onSurface
                                        ),
                                    trailingIcon = {
                                        Row {
                                            TextButton(
                                                onClick = {
                                                    // Save title
                                                    if (progressPhotos.isNotEmpty()) {
                                                        viewModel.updateProgressPhotoTitle(
                                                            progressPhotos.first().id,
                                                            title
                                                        )
                                                    }
                                                    isEditingTitle = false
                                                }
                                            ) {
                                                Text(
                                                    "Guardar",
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                            TextButton(
                                                onClick = {
                                                    // Cancel editing
                                                    title =
                                                        progressPhotos.firstOrNull()?.title
                                                            ?: "Inserta un título"
                                                    isEditingTitle = false
                                                }
                                            ) {
                                                Text(
                                                    "Cancelar",
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                        }
                                    }
                                )
                            } else {
                                Text(
                                    text = title,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier =
                                        Modifier.clickable { isEditingTitle = true }.padding(16.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                            Text(
                                text = formatFullDate(progressPhotos.first().photoDate.toString()),
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 14.sp
                            )
                        }

                        Row(
                            modifier =
                                Modifier.align(Alignment.BottomStart)
                                    .padding(bottom = 16.dp, start = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = { pickImageLauncher.launch("image/*") },
                                colors =
                                    ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                        contentColor = MaterialTheme.colorScheme.onSurface
                                    ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    "Subir desde celular",
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Button(
                                onClick = { takePictureLauncher.launch(null) },
                                colors =
                                    ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                        contentColor = MaterialTheme.colorScheme.onSurface
                                    ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(
                                    Icons.Default.CameraAlt,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(Modifier.width(4.dp))
                                Text("Tomar foto", color = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Anímate a subir tu primera foto",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(
                            onClick = { pickImageLauncher.launch("image/*") },
                            colors =
                                ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(Modifier.width(4.dp))
                            Text("Subir desde celular", color = MaterialTheme.colorScheme.onSurface)
                        }
                        Button(
                            onClick = { takePictureLauncher.launch(null) },
                            colors =
                                ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                Icons.Default.CameraAlt,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(Modifier.width(4.dp))
                            Text("Tomar foto", color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }

            Column(modifier = Modifier.padding(12.dp).fillMaxHeight()) {
                Text(text = "Progreso", fontSize = 18.sp, fontWeight = FontWeight.Medium)

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Ordenar por:",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        TextButton(
                            onClick = {
                                sortBy = if (sortBy == "date_desc") "date_asc" else "date_desc"
                            }
                        ) {
                            Text(
                                text =
                                    when (sortBy) {
                                        "date_desc" -> "Fecha ↓"
                                        "date_asc" -> "Fecha ↑"
                                        else -> "Fecha"
                                    },
                                fontSize = 14.sp
                            )
                        }
                        TextButton(
                            onClick = {
                                sortBy = if (sortBy == "name_asc") "name_desc" else "name_asc"
                            }
                        ) {
                            Text(
                                text =
                                    when (sortBy) {
                                        "name_asc" -> "Título ↑"
                                        "name_desc" -> "Título ↓"
                                        else -> "Título"
                                    },
                                fontSize = 14.sp
                            )
                        }
                    }

                    TextButton(
                        onClick = { viewMode = if (viewMode == "grid") "list" else "grid" }
                    ) {
                        Text(
                            text = if (viewMode == "grid") "Lista" else "Cuadrícula",
                            fontSize = 14.sp
                        )                    }
                }

                if (viewMode == "grid") {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize().weight(1f)
                    ) {
                        items(sortedPhotos) { photo ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Card(
                                    modifier =
                                        Modifier.aspectRatio(1f)
                                            .clip(RoundedCornerShape(12.dp))
                                            .clickable { selectedPhoto = photo },
                                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                ) {
                                    SubcomposeAsyncImage(
                                        model =
                                            ImageRequest.Builder(LocalContext.current)
                                                .data(
                                                    "https://fitscan.onrender.com/assets/${photo.imagePath}"
                                                )
                                                .crossfade(true)
                                                .build(),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize(),
                                        loading = {
                                            Box(
                                                modifier = Modifier.fillMaxSize(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                CircularProgressIndicator(
                                                    modifier = Modifier.size(24.dp),
                                                    color = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        },
                                        error = {
                                            Box(
                                                modifier =
                                                    Modifier.fillMaxSize()
                                                        .background(
                                                            MaterialTheme.colorScheme.surfaceVariant
                                                        ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "⚠",
                                                    fontSize = 20.sp,
                                                    color =
                                                        MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }
                                    )
                                }
                                // Título solo lectura
                                Text(
                                    text = photo.title ?: "Sin título",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    textAlign = TextAlign.Center
                                )

                                Text(
                                    text = formatDate(photo.photoDate.toString()),
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize().weight(1f)
                    ) {
                        items(sortedPhotos) { photo ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { selectedPhoto = photo }
                            ) {
                                Card(
                                    modifier = Modifier.size(80.dp).clip(RoundedCornerShape(12.dp)),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                ) {
                                    SubcomposeAsyncImage(
                                        model =
                                            ImageRequest.Builder(LocalContext.current)
                                                .data(
                                                    "https://fitscan.onrender.com/assets/${photo.imagePath}"
                                                )
                                                .crossfade(true)
                                                .build(),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize(),
                                        loading = {
                                            Box(
                                                modifier = Modifier.fillMaxSize(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                CircularProgressIndicator(
                                                    modifier = Modifier.size(16.dp),
                                                    color = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        },
                                        error = {
                                            Box(
                                                modifier =
                                                    Modifier.fillMaxSize()
                                                        .background(
                                                            MaterialTheme.colorScheme.surfaceVariant
                                                        ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "⚠",
                                                    fontSize = 16.sp,
                                                    color =
                                                        MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = photo.title ?: "Sin título",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = formatDate(photo.photoDate.toString()),
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        selectedPhoto?.let { photo ->
            var editingOverlayTitle by remember { mutableStateOf(false) }
            Box(
                modifier =
                    Modifier.fillMaxSize()
                        .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.9f))
                        .clickable { selectedPhoto = null }
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            onClick = { selectedPhoto = null },
                            modifier =
                                Modifier.background(
                                    MaterialTheme.colorScheme.surface.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(50)
                                )
                        ) {
                            Text(
                                text = "✕",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier =
                            Modifier.fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(16.dp)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        SubcomposeAsyncImage(
                            model =
                                ImageRequest.Builder(LocalContext.current)
                                    .data("https://fitscan.onrender.com/assets/${photo.imagePath}")
                                    .crossfade(true)
                                    .build(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize(),
                            loading = {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(40.dp),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            },
                            error = {
                                Box(
                                    modifier =
                                        Modifier.fillMaxSize()
                                            .background(MaterialTheme.colorScheme.surfaceVariant),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = "⚠",
                                            fontSize = 32.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = "Error al cargar imagen",
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Título editable
                    if (editingOverlayTitle) {
                        OutlinedTextField(
                            value = editingPhotoTitle,
                            onValueChange = { editingPhotoTitle = it },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            textStyle =
                                LocalTextStyle.current.copy(
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 18.sp,
                                    textAlign = TextAlign.Center
                                ),
                            colors =
                                OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.onSurface,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                                    cursorColor = MaterialTheme.colorScheme.onSurface
                                ),
                            trailingIcon = {
                                Row {
                                    TextButton(
                                        onClick = {
                                            viewModel.updateProgressPhotoTitle(
                                                photo.id,
                                                editingPhotoTitle
                                            )
                                            selectedPhoto = photo.copy(title = editingPhotoTitle)
                                            editingOverlayTitle = false
                                        }
                                    ) {
                                        Text("Guardar", color = MaterialTheme.colorScheme.onSurface)
                                    }
                                    TextButton(
                                        onClick = {
                                            editingOverlayTitle = false
                                            editingPhotoTitle = photo.title ?: ""
                                        }
                                    ) {
                                        Text(
                                            "Cancelar",
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        )
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = photo.title ?: "Sin título",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier =
                                    Modifier.clickable {
                                        editingOverlayTitle = true
                                        editingPhotoTitle = photo.title ?: ""
                                    }
                                        .padding(vertical = 16.dp, horizontal = 8.dp)
                            )
                            IconButton(
                                onClick = {
                                    editingOverlayTitle = true
                                    editingPhotoTitle = photo.title ?: ""
                                },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Editar título",
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                    Text(
                        text = formatFullDate(photo.photoDate.toString()),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botón de eliminar
                    Button(
                        onClick = {
                            photoToDelete = photo
                            showDeleteDialog = true
                        },
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.8f),
                                contentColor = MaterialTheme.colorScheme.onError
                            ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(horizontal = 32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar foto",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Eliminar foto")
                    }
                }
            }
        }
    }

    if (showDeleteDialog && photoToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                photoToDelete = null
            },
            title = { Text("¿Eliminar foto?") },
            text = { Text("Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        photoToDelete?.let { photo -> viewModel.deleteProgressPhoto(photo.id) }
                        showDeleteDialog = false
                        photoToDelete = null
                        selectedPhoto = null
                    }
                ) {
                    Text("Eliminar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        photoToDelete = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

private fun saveBitmapToTempFile(context: android.content.Context, bitmap: Bitmap): Uri? {
    return try {
        val file =
            java.io.File(context.cacheDir, "temp_progress_photo_${System.currentTimeMillis()}.jpg")
        val outputStream = java.io.FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
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

private fun formatDate(dateString: String): String {
    return try {
        val parts = dateString.split("-", "T", " ")
        if (parts.size >= 3) {
            val month =
                when (parts[1]) {
                    "01" -> "Ene"
                    "02" -> "Feb"
                    "03" -> "Mar"
                    "04" -> "Abr"
                    "05" -> "May"
                    "06" -> "Jun"
                    "07" -> "Jul"
                    "08" -> "Ago"
                    "09" -> "Sep"
                    "10" -> "Oct"
                    "11" -> "Nov"
                    "12" -> "Dic"
                    else -> parts[1]
                }
            "${parts[2]} $month"
        } else {
            dateString.take(10)
        }
    } catch (e: Exception) {
        dateString.take(10)
    }
}

private fun formatFullDate(dateString: String): String {
    return try {
        val parts = dateString.split("-", "T", " ")
        if (parts.size >= 3) {
            "${parts[2]}.${parts[1]}.${parts[0]}"
        } else dateString.take(10)
    } catch (e: Exception) {
        dateString.take(10)
    }
}

@Preview
@Composable
fun ProgressPhotoScreenPreview() {
    ProgressPhotoScreen(navController = rememberNavController())
}
