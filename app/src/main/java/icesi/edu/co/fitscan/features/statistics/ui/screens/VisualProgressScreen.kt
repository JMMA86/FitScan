package icesi.edu.co.fitscan.features.statistics.ui.screens

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import icesi.edu.co.fitscan.features.auth.ui.screens.SectionTitle
import icesi.edu.co.fitscan.features.common.ui.components.FitScanHeader
import icesi.edu.co.fitscan.features.common.ui.viewmodel.AppState
import icesi.edu.co.fitscan.features.statistics.data.remote.ProgressPhoto
import icesi.edu.co.fitscan.features.statistics.ui.viewmodel.VisualProgressViewModel

@Composable
fun VisualProgressScreen(
    viewmodel: VisualProgressViewModel = viewModel(),
    navController: NavController = rememberNavController()
) {
    val context = LocalContext.current
    val progressPhotos by viewmodel.progressPhotos.collectAsState()
    val isUploading by viewmodel.isUploading.collectAsState()

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewmodel.setImageURL(it.toString())
            viewmodel.uploadProgressPhoto(context, it, "Progreso visual")
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
            else Button(
                onClick = { pickImageLauncher.launch("image/*") },
                enabled = !isUploading
            ) { Text("Agregar foto de progreso") }

            LazyVerticalGrid (
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                columns = GridCells.Adaptive(minSize = 128.dp),
            ) {
                items(progressPhotos.size) { index ->
                    AsyncImage(
                        model  = "https://fitscan.onrender.com/assets/${progressPhotos[index].image_path}?access_token=${AppState.token}",
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(CircleShape)
                    )
                }
            }
        }
    }
}
