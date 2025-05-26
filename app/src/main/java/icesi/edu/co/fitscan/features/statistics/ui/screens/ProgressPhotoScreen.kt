package icesi.edu.co.fitscan.features.statistics.ui.screens

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import icesi.edu.co.fitscan.features.statistics.ui.viewmodel.ProgressPhotoViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.TextButton
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import icesi.edu.co.fitscan.features.common.ui.components.FitScanHeader
import icesi.edu.co.fitscan.features.common.ui.viewmodel.AppState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import icesi.edu.co.fitscan.features.statistics.ui.viewmodel.factory.ProgressPhotoViewModelFactory

@Composable
fun ProgressPhotoScreen(
    navController: NavController = rememberNavController()
) {
    val viewModel: ProgressPhotoViewModel = viewModel(factory = ProgressPhotoViewModelFactory())
    val context = LocalContext.current
    val progressPhotos by viewModel.progressPhotos.collectAsState()
    val isUploading by viewModel.isUploading.collectAsState()

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.uploadProgressPhoto(context, it, "Progreso visual")
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        FitScanHeader(
            title = "Progreso visual",
            navController = navController
        )
        Column(modifier=Modifier.padding(horizontal=8.dp, vertical=16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            if(isUploading) CircularProgressIndicator()
            else TextButton (
                onClick = { pickImageLauncher.launch("image/*") },
                enabled = !isUploading
            ) { Text("Agregar foto de progreso") }

            LazyVerticalGrid (
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                columns = GridCells.Adaptive(minSize = 128.dp)
            ) {
                items(progressPhotos.size) { index ->
                    Box(modifier= Modifier.padding(8.dp)) {
                        Card {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                AsyncImage(
                                    model  = "https://fitscan.onrender.com/assets/${progressPhotos[index].imagePath}",
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .fillMaxWidth()
                                        .aspectRatio(1f)
                                        .clip(CircleShape)
                                )
                                Text(text=progressPhotos[index].photoDate.toString())
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
@Preview
fun ProgressPhotoScreenPreview() {
    ProgressPhotoScreen(navController = rememberNavController())
}