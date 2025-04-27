package icesi.edu.co.fitscan.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF81C784),
    secondary = Color(0xFF4CAF50),
    tertiary = Color(0xFF2E7D32),
    surface = Color(0xFF1C1B1F),
    onSurface = Color(0xFFE8F5E9),
    surfaceVariant = Color(0xFF2E7D32),
    onSurfaceVariant = Color(0xFFE8F5E9),
    primaryContainer = Color(0xFF2E7D32),
    onPrimaryContainer = Color(0xFFE8F5E9)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF4CAF50), // Verde similar al de tu login
    secondary = Color(0xFF81C784),
    tertiary = Color(0xFFA5D6A7),
    surface = Color(0xFFFFFBFE),
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFFE8F5E9),
    onSurfaceVariant = Color(0xFF2E7D32),
    primaryContainer = Color(0xFFA5D6A7),
    onPrimaryContainer = Color(0xFF1B5E20)
)

@Composable
fun FitScanTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}