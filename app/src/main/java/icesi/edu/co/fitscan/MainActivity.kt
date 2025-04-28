package icesi.edu.co.fitscan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import icesi.edu.co.fitscan.features.auth.ui.screens.PersonalDataScreen
import icesi.edu.co.fitscan.features.auth.ui.screens.RegisterScreen
import icesi.edu.co.fitscan.navigation.NavigationHost
import icesi.edu.co.fitscan.ui.theme.FitScanTheme
import icesi.edu.co.fitscan.features.common.ui.components.FitScanHeader
import icesi.edu.co.fitscan.features.common.ui.components.FitScanNavBar
import icesi.edu.co.fitscan.features.common.ui.viewmodel.AppState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FitScanTheme {
                App()
            }
        }
    }
}

@Composable
fun App() {
    val navController = rememberNavController()
    val showHeader by AppState.showHeader.collectAsState()
    Scaffold (
        topBar = { if(showHeader) FitScanHeader(
            title="FitScanAI",
            onBackClick = { /* TODO */ },
            navController=navController
        ) else Box{} },
        bottomBar = { if(showHeader) FitScanNavBar(navController) else Box {} }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            NavigationHost(navController)
        }
    }
}

// @Preview(showBackground = true)
// @Composable
// fun GreetingPreview() {
//    FitScanTheme {
//         App()
//     }
// }

//@Preview(showBackground = true)
//@Composable
//fun LoginScreenPreview() {
//    FitScanTheme {
//        LoginScreen(Color(0xFF4CAF50))
//    }
//}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    FitScanTheme {
        RegisterScreen(Color(0xFF4CAF50))
    }
}

@Preview(showBackground = true)
@Composable
fun PersonalDataScreenPreview() {
    FitScanTheme {
        PersonalDataScreen(Color(0xFF4CAF50))
    }
}
