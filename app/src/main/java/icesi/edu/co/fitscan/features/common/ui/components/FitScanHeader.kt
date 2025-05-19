package icesi.edu.co.fitscan.features.common.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import icesi.edu.co.fitscan.navigation.Screen
import icesi.edu.co.fitscan.ui.theme.greyMed
import icesi.edu.co.fitscan.ui.theme.greenLess
import icesi.edu.co.fitscan.ui.theme.greyStrong

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitScanHeader(
    title: String,
    showBackIcon: Boolean = true,
    navController: NavController
) {
    TopAppBar(
        title = { Text(text = title, color = Color.White) },
        navigationIcon = {
            if(showBackIcon) IconButton (
                onClick = { navController.popBackStack() } ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            } else Box {}
        },
        actions = {
            /*
            Button(
                onClick = { navController.navigate(Screen.Login.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = greenLess)
            ) {
                /* Text("Iniciar Sesión", color = Color.White) */
            }
             */
        },
        colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
            containerColor = greyStrong
        ),
        modifier = Modifier.fillMaxWidth()
    )
}