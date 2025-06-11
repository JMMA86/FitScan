package icesi.edu.co.fitscan

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import icesi.edu.co.fitscan.features.auth.ui.screens.PersonalDataScreen
import icesi.edu.co.fitscan.features.auth.ui.screens.RegisterScreen
import icesi.edu.co.fitscan.features.common.data.local.MultipartProvider
import icesi.edu.co.fitscan.navigation.NavigationHost
import icesi.edu.co.fitscan.ui.theme.FitScanTheme
import icesi.edu.co.fitscan.features.common.ui.components.FitScanNavBar
import icesi.edu.co.fitscan.navigation.Screen
import icesi.edu.co.fitscan.notification.NotificationUtil

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions(
            arrayOf(
                android.Manifest.permission.POST_NOTIFICATIONS,
                android.Manifest.permission.CAMERA
            ), 1
        )

        Firebase.messaging.subscribeToTopic("noti").addOnSuccessListener {
            Log.e(">>>","Suscrito")
        }
        
        enableEdgeToEdge()
        MultipartProvider.init(applicationContext)
        
        // Cancelar notificación pendiente cuando se abre la app
        NotificationUtil.cancelInactivityNotification(this)
        
        setContent {
            FitScanTheme {
                App()
            }
        }
    }    override fun onStop() {
        super.onStop()
        // Programar notificación cuando el usuario sale completamente de la app
        NotificationUtil.scheduleInactivityNotification(this, 30)
    }

    override fun onRestart() {
        super.onRestart()
        // Cancelar notificación cuando el usuario regresa a la app
        NotificationUtil.cancelInactivityNotification(this)
    }
}

@Composable
fun App() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val authRoutes = setOf(
        Screen.Login.route,
        Screen.Registration.route,
        Screen.BodyMeasurements.route
    )
    val showHeaderAndNavBar = currentRoute != null && authRoutes.none { currentRoute.startsWith(it) }

    Scaffold (
        bottomBar = {
            if (showHeaderAndNavBar) FitScanNavBar(navController) else Box {}
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            NavigationHost(navController)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    FitScanTheme {
        RegisterScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun PersonalDataScreenPreview() {
    FitScanTheme {
        PersonalDataScreen()
    }
}
