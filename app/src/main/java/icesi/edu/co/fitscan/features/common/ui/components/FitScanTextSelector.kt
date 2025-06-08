package icesi.edu.co.fitscan.features.common.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.DpOffset
import androidx.compose.material3.MaterialTheme

@Composable
fun FitScanTextSelector(
    value: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Selecciona una opción"
) {
    val expanded = remember { mutableStateOf(false) }
    val selectorSize = remember { mutableStateOf(IntSize.Zero) }
    val density = LocalDensity.current
    Box(modifier = modifier) {        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(10.dp))
                .clickable { expanded.value = true }
                .padding(horizontal = 16.dp, vertical = 14.dp)
                .onGloballyPositioned { coordinates ->
                    selectorSize.value = coordinates.size
                }
        ) {
            Text(
                text = if (value.isNotEmpty()) value else placeholder,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
        }
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .width(with(density) { selectorSize.value.width.toDp() }),
            offset = DpOffset(x = 0.dp, y = with(density) { selectorSize.value.height.toDp() })
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        onOptionSelected(option)
                        expanded.value = false
                    },
                    text = {
                        Text(
                            text = option,
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                )
            }
        }
    }
}
