package icesi.edu.co.fitscan.features.common.ui.components

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
import androidx.navigation.NavController
import icesi.edu.co.fitscan.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitScanHeader(
    title: String,
    onBackClick: () -> Unit,
    navController: NavController
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton (onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            Button(
                onClick = { navController.navigate(Screen.Login.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            ) {
                Text("Iniciar Sesión")
            }
        },

        modifier = Modifier.fillMaxWidth()
    )
}