package icesi.edu.co.fitscan.ui.theme

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
    primary = Green300,
    onPrimary = Grey900,
    primaryContainer = Green700,
    onPrimaryContainer = Color.White,
    
    secondary = Green400,
    onSecondary = Grey900,
    secondaryContainer = Green900,
    onSecondaryContainer = Color.White,
    
    tertiary = Green700,
    onTertiary = Color.White,
    tertiaryContainer = Green900,
    onTertiaryContainer = Green300,
    
    error = Red400,
    onError = Grey900,
    errorContainer = Red700,
    onErrorContainer = Color.White,
    
    background = DarkBackground,
    onBackground = Color.White,
    surface = DarkSurface,
    onSurface = Color.White,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Grey400,
    
    outline = Grey600,
    outlineVariant = Grey700,
    scrim = Color.Black,
    
    inverseSurface = Grey100,
    inverseOnSurface = Grey900,
    inversePrimary = Green700
)

private val LightColorScheme = lightColorScheme(
    primary = Green700,
    onPrimary = Color.White,
    primaryContainer = Green300,
    onPrimaryContainer = Green900,
    
    secondary = Green700,
    onSecondary = Color.White,
    secondaryContainer = Green300,
    onSecondaryContainer = Green900,
    
    tertiary = Green900,
    onTertiary = Color.White,
    tertiaryContainer = Green300,
    onTertiaryContainer = Green900,
    
    error = Red700,
    onError = Color.White,
    errorContainer = Red400,
    onErrorContainer = Grey900,
    
    background = LightBackground,
    onBackground = Grey900,
    surface = LightSurface,
    onSurface = Grey900,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = Grey700,
    
    outline = Grey400,
    outlineVariant = Grey300,
    scrim = Color.Black,
    
    inverseSurface = Grey800,
    inverseOnSurface = Grey100,
    inversePrimary = Green300
)

@Composable
fun FitScanTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disabled by default to maintain brand colors
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