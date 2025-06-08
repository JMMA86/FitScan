package icesi.edu.co.fitscan.features.statistics.ui.screens

import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
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
import coil.request.ImageRequest
import coil.compose.SubcomposeAsyncImage
import androidx.compose.material3.CircularProgressIndicator
import icesi.edu.co.fitscan.features.common.ui.components.FitScanHeader
import icesi.edu.co.fitscan.features.statistics.ui.viewmodel.ProgressPhotoViewModel
import icesi.edu.co.fitscan.features.statistics.ui.viewmodel.factory.ProgressPhotoViewModelFactory

@Composable
fun ProgressPhotoScreen(
    navController: NavController = rememberNavController()
) {
    val viewModel: ProgressPhotoViewModel = viewModel(factory = ProgressPhotoViewModelFactory())
    val context = LocalContext.current
    val progressPhotos by viewModel.progressPhotos.collectAsState()
    var title by remember { mutableStateOf("Añadir un título") }
    var isEditingTitle by remember { mutableStateOf(false) }
    var viewMode by remember { mutableStateOf("grid") }
    var sortBy by remember { mutableStateOf("date_desc") }
    var selectedPhoto by remember { mutableStateOf<icesi.edu.co.fitscan.domain.model.ProgressPhoto?>(null) }
    var editingPhotoId by remember { mutableStateOf<String?>(null) }
    var editingPhotoTitle by remember { mutableStateOf("") }

    // Update title when photos change
    LaunchedEffect(progressPhotos) {
        if (progressPhotos.isNotEmpty()) {
            title = progressPhotos.first().title ?: "Añadir un título"
        }
    }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.uploadProgressPhoto(context, it, title)
        }
    }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            try {
                val tempUri = saveBitmapToTempFile(context, bitmap)
                tempUri?.let { uri ->
                    viewModel.uploadProgressPhoto(context, uri, title)
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error procesando imagen: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "No se pudo capturar la imagen", Toast.LENGTH_SHORT).show()
        }
    }

    val sortedPhotos = remember(progressPhotos, sortBy) {
        when (sortBy) {
            "date_asc" -> progressPhotos.sortedBy { it.photoDate }
            "name_asc" -> progressPhotos.sortedBy { it.title ?: "" }
            "name_desc" -> progressPhotos.sortedByDescending { it.title ?: "" }
            else -> progressPhotos.sortedByDescending { it.photoDate }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            FitScanHeader(title = "Toma una foto de las gains 🔥🔥🔥", navController = navController)

            if (progressPhotos.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                ) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("https://fitscan.onrender.com/assets/${progressPhotos.first().imagePath}")
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
                                CircularProgressIndicator(color = Color.White)
                            }
                        },
                        error = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Gray),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Error cargando imagen",
                                    color = Color.White,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.4f))
                    ) {
                        // Title section centered both vertically and horizontally
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (isEditingTitle) {
                                OutlinedTextField(
                                    value = title,
                                    onValueChange = { title = it },
                                    singleLine = true,
                                    modifier = Modifier.padding(16.dp),
                                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color.White,
                                        unfocusedBorderColor = Color.White,
                                        cursorColor = Color.White
                                    ),
                                    trailingIcon = {
                                        Row {
                                            TextButton(
                                                onClick = {
                                                    // Save title
                                                    if (progressPhotos.isNotEmpty()) {
                                                        viewModel.updateProgressPhotoTitle(progressPhotos.first().id, title)
                                                    }
                                                    isEditingTitle = false
                                                }
                                            ) {
                                                Text("Guardar", color = Color.White)
                                            }
                                            TextButton(
                                                onClick = {
                                                    // Cancel editing
                                                    title = progressPhotos.firstOrNull()?.title ?: "Añadir un título"
                                                    isEditingTitle = false
                                                }
                                            ) {
                                                Text("Cancelar", color = Color.White)
                                            }
                                        }
                                    }
                                )
                            } else {
                                Text(
                                    text = title,
                                    color = Color.White,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .clickable { isEditingTitle = true }
                                        .padding(16.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                            Text(
                                text = formatFullDate(progressPhotos.first().photoDate.toString()),
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(bottom = 16.dp, start = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = { pickImageLauncher.launch("image/*") },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.DarkGray,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                                Spacer(Modifier.width(4.dp))
                                Text("Subir desde celular", color = Color.White)
                            }
                            Button(
                                onClick = { takePictureLauncher.launch(null) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.DarkGray,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color.White)
                                Spacer(Modifier.width(4.dp))
                                Text("Tomar foto", color = Color.White)
                            }
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Anímate a subir tu primera foto",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { pickImageLauncher.launch("image/*") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.DarkGray,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                            Spacer(Modifier.width(4.dp))
                            Text("Subir desde celular", color = Color.White)
                        }
                        Button(
                            onClick = { takePictureLauncher.launch(null) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.DarkGray,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color.White)
                            Spacer(Modifier.width(4.dp))
                            Text("Tomar foto", color = Color.White)
                        }
                    }
                }
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Progreso",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TextButton(onClick = {
                            sortBy = if (sortBy == "date_desc") "date_asc" else "date_desc"
                        }) { Text("Fecha") }
                        TextButton(onClick = {
                            sortBy = if (sortBy == "name_asc") "name_desc" else "name_asc"
                        }) { Text("Nombre") }
                        TextButton(onClick = {
                            viewMode = if (viewMode == "grid") "list" else "grid"
                        }) { Text(if (viewMode == "grid") "Lista" else "Cuadrícula") }
                    }
                }

                if (viewMode == "grid") {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        items(sortedPhotos) { photo ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Card(
                                    modifier = Modifier
                                        .aspectRatio(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable { selectedPhoto = photo },
                                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                ) {
                                    SubcomposeAsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
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
                                                    modifier = Modifier.size(24.dp),
                                                    color = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        },
                                        error = {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .background(Color.LightGray),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "⚠",
                                                    fontSize = 20.sp,
                                                    color = Color.Gray
                                                )
                                            }
                                        }
                                    )
                                }
                                
                                // Título editable
                                if (editingPhotoId == photo.id) {
                                    OutlinedTextField(
                                        value = editingPhotoTitle,
                                        onValueChange = { editingPhotoTitle = it },
                                        singleLine = true,
                                        modifier = Modifier.width(120.dp),
                                        textStyle = LocalTextStyle.current.copy(fontSize = 12.sp),
                                        trailingIcon = {
                                            Row {
                                                IconButton(
                                                    onClick = {
                                                        viewModel.updateProgressPhotoTitle(photo.id, editingPhotoTitle)
                                                        editingPhotoId = null
                                                    },
                                                    modifier = Modifier.size(20.dp)
                                                ) {
                                                    Text("✓", fontSize = 12.sp, color = Color.Green)
                                                }
                                                IconButton(
                                                    onClick = {
                                                        editingPhotoId = null
                                                        editingPhotoTitle = ""
                                                    },
                                                    modifier = Modifier.size(20.dp)
                                                ) {
                                                    Text("✕", fontSize = 12.sp, color = Color.Red)
                                                }
                                            }
                                        }
                                    )
                                } else {
                                    Text(
                                        text = photo.title ?: "Sin título",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.clickable {
                                            editingPhotoId = photo.id
                                            editingPhotoTitle = photo.title ?: ""
                                        },
                                        textAlign = TextAlign.Center
                                    )
                                }
                                
                                Text(
                                    text = formatDate(photo.photoDate.toString()),
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        sortedPhotos.forEach { photo ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { selectedPhoto = photo }
                            ) {
                                Card(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(RoundedCornerShape(12.dp)),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                ) {
                                    SubcomposeAsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
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
                                                    modifier = Modifier.size(16.dp),
                                                    color = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        },
                                        error = {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .background(Color.LightGray),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "⚠",
                                                    fontSize = 16.sp,
                                                    color = Color.Gray
                                                )
                                            }
                                        }
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    if (editingPhotoId == photo.id) {
                                        OutlinedTextField(
                                            value = editingPhotoTitle,
                                            onValueChange = { editingPhotoTitle = it },
                                            singleLine = true,
                                            modifier = Modifier.width(200.dp),
                                            textStyle = LocalTextStyle.current.copy(fontSize = 14.sp),
                                            trailingIcon = {
                                                Row {
                                                    IconButton(
                                                        onClick = {
                                                            viewModel.updateProgressPhotoTitle(photo.id, editingPhotoTitle)
                                                            editingPhotoId = null
                                                        }
                                                    ) {
                                                        Text("✓", color = Color.Green)
                                                    }
                                                    IconButton(
                                                        onClick = {
                                                            editingPhotoId = null
                                                            editingPhotoTitle = ""
                                                        }
                                                    ) {
                                                        Text("✕", color = Color.Red)
                                                    }
                                                }
                                            }
                                        )
                                    } else {
                                        Text(
                                            text = photo.title ?: "Sin título",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.clickable {
                                                editingPhotoId = photo.id
                                                editingPhotoTitle = photo.title ?: ""
                                            }
                                        )
                                    }
                                    Text(
                                        text = formatDate(photo.photoDate.toString()),
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        selectedPhoto?.let { photo ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.9f))
                    .clickable { selectedPhoto = null }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            onClick = { selectedPhoto = null },
                            modifier = Modifier
                                .background(
                                    Color.White.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(50)
                                )
                        ) {
                            Text(
                                text = "✕",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(16.dp)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
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
                                        color = Color.White
                                    )
                                }
                            },
                            error = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Gray),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "⚠",
                                            fontSize = 32.sp,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "Error al cargar imagen",
                                            color = Color.White,
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Título editable
                    if (editingPhotoId == photo.id) {
                        OutlinedTextField(
                            value = editingPhotoTitle,
                            onValueChange = { editingPhotoTitle = it },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = LocalTextStyle.current.copy(
                                color = Color.White,
                                fontSize = 18.sp,
                                textAlign = TextAlign.Center
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.White,
                                unfocusedBorderColor = Color.White,
                                cursorColor = Color.White
                            ),
                            trailingIcon = {
                                Row {
                                    TextButton(
                                        onClick = {
                                            viewModel.updateProgressPhotoTitle(photo.id, editingPhotoTitle)
                                            editingPhotoId = null
                                        }
                                    ) {
                                        Text("Guardar", color = Color.White)
                                    }
                                    TextButton(
                                        onClick = {
                                            editingPhotoId = null
                                            editingPhotoTitle = ""
                                        }
                                    ) {
                                        Text("Cancelar", color = Color.White)
                                    }
                                }
                            }
                        )
                    } else {
                        Text(
                            text = photo.title ?: "Sin título",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .clickable {
                                    editingPhotoId = photo.id
                                    editingPhotoTitle = photo.title ?: ""
                                }
                                .padding(16.dp)
                        )
                    }
                    
                    Text(
                        text = formatFullDate(photo.photoDate.toString()),
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

private fun saveBitmapToTempFile(context: android.content.Context, bitmap: Bitmap): Uri? {
    return try {
        val file = java.io.File(context.cacheDir, "temp_progress_photo_${System.currentTimeMillis()}.jpg")
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
            val month = when (parts[1]) {
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