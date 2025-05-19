package icesi.edu.co.fitscan.features.common.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import icesi.edu.co.fitscan.ui.theme.greyMed
import icesi.edu.co.fitscan.ui.theme.greenLess

@Composable
fun StatisticCard(
    title: String,
    subtitle: String,
    iconRes: Int,
    iconBackground: Color = Color.Unspecified,
    iconColor: Color = Color.White,
    onClick: () -> Unit = {},
) {
    Surface(
        color = greyMed,
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(iconBackground, shape = RoundedCornerShape(50))
                    .padding(10.dp)
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(subtitle, color = Color.White, fontSize = 14.sp)
            }
            Box(
                modifier = Modifier
                    .background(greenLess, shape = RoundedCornerShape(50))
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = greyMed,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
