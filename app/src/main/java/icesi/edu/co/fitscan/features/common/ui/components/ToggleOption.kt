package icesi.edu.co.fitscan.features.common.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ToggleOption(
    label: String,
    selected: Boolean = false,
    onSelected: () -> Unit,
    options: List<String>,
    icons: List<Int>,
    modifier: Modifier
) {
    OptionButton(
        label = label,
        selected = selected,
        onClick = onSelected,
        modifier = modifier,
        options = options,
        icons = icons
    )
}