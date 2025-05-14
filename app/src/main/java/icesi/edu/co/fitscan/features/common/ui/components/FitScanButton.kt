package icesi.edu.co.fitscan.features.common.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import icesi.edu.co.fitscan.ui.theme.greenLess

@Composable
fun FitScanButton(onClick: () -> Unit, icon: Int) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = greenLess,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text("Crear entrenamiento", fontSize = 16.sp)
    }
}
