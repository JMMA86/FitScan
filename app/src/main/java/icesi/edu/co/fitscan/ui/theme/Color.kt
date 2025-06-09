package icesi.edu.co.fitscan.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.MaterialTheme

// Base colors that work for both themes

val Green50 = Color(0xFFF7FDF7)
val Green100 = Color(0xFFDDEADD)
val Green200 = Color(0xFFA5D6A7)
val Green300 = Color(0xFF4CAF50)
val Green400 = Color(0xFF66BB6A)
val Green700 = Color(0xFF388E3C)
val Green900 = Color(0xFF1B5E20)

val Red400 = Color(0xFFEF5350)
val Red700 = Color(0xFFD32F2F)

val Grey50 = Color(0xFFFAFAFA)
val Grey100 = Color(0xFFF5F5F5)
val Grey200 = Color(0xFFEEEEEE)
val Grey300 = Color(0xFFE0E0E0)
val Grey400 = Color(0xFFBDBDBD)
val Grey500 = Color(0xFF9E9E9E)
val Grey600 = Color(0xFF757575)
val Grey700 = Color(0xFF616161)
val Grey800 = Color(0xFF424242)
val Grey850 = Color(0xFF303030)
val Grey900 = Color(0xFF212121)

// Dark theme specific colors
val DarkBackground = Color(0xFF101414)
val DarkSurface = Color(0xFF101414)
val DarkSurfaceVariant = Color(0xFF2B2B2A)
val DarkCard = Color(0xFF284048)
val DarkButton = Color(0xFF3F4946)

// Light theme specific colors
val LightBackground = Green50
val LightSurface = Green50
val LightSurfaceVariant = Green100
val LightCard = Green300

// Dirty colors 
@Deprecated("Use MaterialTheme.colorScheme instead")
val greyStrong = DarkBackground
@Deprecated("Use MaterialTheme.colorScheme instead")
val greyMed = DarkSurface
@Deprecated("Use MaterialTheme.colorScheme instead")
val greyTrueLight = DarkSurfaceVariant
@Deprecated("Use MaterialTheme.colorScheme instead")
val greyLight = DarkCard
@Deprecated("Use MaterialTheme.colorScheme instead")
val greySuperLight = Grey400
@Deprecated("Use MaterialTheme.colorScheme instead")
val greenLess = Green300
@Deprecated("Use MaterialTheme.colorScheme instead")
val backgroundGrey = DarkBackground
@Deprecated("Use MaterialTheme.colorScheme instead")
val greyButton = DarkButton
@Deprecated("Use MaterialTheme.colorScheme instead")
val redDangerous = Red700
@Deprecated("Use MaterialTheme.colorScheme instead")
val dashboardCardBackground = Color(0xFF2F483C)
@Deprecated("Use MaterialTheme.colorScheme instead")
val dashboardGreen = Color(0xFF00FF7F)

// Semantic color extensions for theme-aware colors
val ColorScheme.cardBackground: Color
    get() = surfaceVariant

val ColorScheme.buttonBackground: Color  
    get() = primaryContainer

val ColorScheme.success: Color
    get() = Green300

val ColorScheme.warning: Color
    get() = Color(0xFFFF9800)

val ColorScheme.chartPrimary: Color
    get() = primary

val ColorScheme.chartSecondary: Color
    get() = Color(0xFF81C784) // Light green for secondary chart data

val ColorScheme.chartTertiary: Color
    get() = Color(0xFF4CAF50) // Primary green for main chart data

val ColorScheme.progressTrack: Color
    get() = outline

val ColorScheme.textSecondary: Color
    get() = onSurfaceVariant

val ColorScheme.iconTint: Color
    get() = onSurface