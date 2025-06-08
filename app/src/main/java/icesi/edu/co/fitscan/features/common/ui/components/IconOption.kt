package icesi.edu.co.fitscan.features.common.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun IconOption(
    label: String,
    options: List<String>,
    icons: List<Int>,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    tint: Color = Color.Unspecified
) {
    if (options.size != icons.size) {
        return
    }

    val index = options.indexOfFirst { it == label }

    if (index != -1 && index < icons.size) {        Icon(
            painter = painterResource(id = icons[index]),
            contentDescription = null,
            tint = if (tint == Color.Unspecified) MaterialTheme.colorScheme.onBackground else tint,
            modifier = modifier.size(size)
        )
    }
}