package icesi.edu.co.fitscan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import icesi.edu.co.fitscan.features.task.ui.screens.LoginScreen
import icesi.edu.co.fitscan.features.task.ui.screens.PersonalDataScreen
import icesi.edu.co.fitscan.features.task.ui.screens.RegisterScreen
import icesi.edu.co.fitscan.ui.theme.FitScanTheme

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
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(Color(0xFF4CAF50)) }
    }
}

// @Preview(showBackground = true)
// @Composable
// fun GreetingPreview() {
//    FitScanTheme {
//         App()
//     }
// }

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    FitScanTheme {
        LoginScreen(Color(0xFF4CAF50))
    }
}

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
