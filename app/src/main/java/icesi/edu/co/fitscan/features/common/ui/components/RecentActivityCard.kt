package icesi.edu.co.fitscan.features.common.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import icesi.edu.co.fitscan.R
import icesi.edu.co.fitscan.ui.theme.dashboardCardBackground
import icesi.edu.co.fitscan.ui.theme.dashboardGreen
import icesi.edu.co.fitscan.ui.theme.greySuperLight

@Composable
fun RecentActivityCard(
    title: String,
    time: String,
    level: String,
    exercises: String
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray.copy(alpha = 0.8f)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 10.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_fitness),
                    contentDescription = "Tipo de ejercicio",
                    tint = dashboardGreen,
                    modifier = Modifier
                        .size(25.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.ic_time),
                            contentDescription = "Duración",
                            tint = greySuperLight,
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = time,
                            style = MaterialTheme.typography.bodyLarge,
                            color = greySuperLight
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Icon(
                            painter = painterResource(R.drawable.ic_fire),
                            contentDescription = "Nivel",
                            tint = greySuperLight,
                            modifier = Modifier.size(16.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = level,
                            style = MaterialTheme.typography.bodyLarge,
                            color = greySuperLight
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Surface(
                            shape = RoundedCornerShape(50),
                            color = dashboardCardBackground
                        ) {
                            Text(
                                text = exercises,
                                color = dashboardGreen,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}