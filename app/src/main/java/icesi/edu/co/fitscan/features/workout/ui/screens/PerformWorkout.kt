package icesi.edu.co.fitscan.features.workout.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import icesi.edu.co.fitscan.R
import icesi.edu.co.fitscan.ui.theme.Dimensions
import icesi.edu.co.fitscan.ui.theme.backgroundGrey
import icesi.edu.co.fitscan.ui.theme.greenLess
import icesi.edu.co.fitscan.ui.theme.greyMed
import icesi.edu.co.fitscan.ui.theme.greyTrueLight

@Composable
fun PerformWorkoutScreen(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier
            .background(backgroundGrey)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimensions.MediumPadding)
            ) {
                Text(
                    text = "Fuerza de todo el cuerpo",
                    fontSize = Dimensions.XLargeTextSize,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(Dimensions.SmallPadding))
                Text(
                    text = "Subtítulos",
                    fontSize = Dimensions.SmallTextSize,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(Dimensions.SmallPadding))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(Dimensions.MediumCornerRadius))
                        .background(greyMed)
                        .padding(Dimensions.SmallPadding)
                ) {
                    Text(
                        text = "3/8 ejercicios completados",
                        fontSize = Dimensions.MediumTextSize,
                        color = Color.White
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimensions.MediumPadding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(Dimensions.MediumCornerRadius))
                        .background(
                            greyTrueLight
                        )
                        .padding(Dimensions.SmallPadding)
                ) {
                    Row(
                        modifier = Modifier
                            .wrapContentSize()
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Pull up",
                            fontSize = Dimensions.LargeTextSize,
                            color = Color.White,
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "10:32",
                            fontSize = Dimensions.LargeTextSize,
                            color = greenLess,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(Dimensions.SmallPadding))
                    Text(
                        text = "Serie 2 de 4",
                        color = greenLess
                    )
                    Spacer(modifier = Modifier.height(Dimensions.SmallPadding))
                    Text(
                        text = "Quedan 55 segundos",
                        color = Color.White
                    )
                }
            }

            Column(
                modifier = Modifier.padding(Dimensions.MediumPadding)
            ) {
                Text(
                    text = "Siguiente",
                    color = Color.White
                )
                Text(
                    text = "Nombre del ejercicio",
                    color = Color.White,
                    fontSize = Dimensions.LargeTextSize,
                    fontWeight = FontWeight.Bold
                )
                Row {
                    Text(
                        text = "4 sets | 12 reps",
                        color = Color.White,
                    )
                }

                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Terminar set")
                }

                Spacer(modifier = Modifier.height(Dimensions.smallestPadding))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {},
                        modifier = Modifier
                            .size(Dimensions.LargeIconSize)
                            .clip(RoundedCornerShape(Dimensions.SmallCornerRadius)),
                        contentPadding = PaddingValues(0.dp),
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Anterior",
                            modifier = Modifier.size(Dimensions.MediumIconSize),
                        )
                    }
                    Spacer(modifier = Modifier.width(Dimensions.smallestPadding))
                    Button(
                        onClick = {},
                        modifier = Modifier
                            .size(Dimensions.LargeIconSize)
                            .clip(RoundedCornerShape(Dimensions.SmallCornerRadius)),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.pause_outline_filled),
                            contentDescription = "Detener workout",
                            modifier = Modifier
                                .size(Dimensions.MediumIconSize)
                        )
                    }
                    Spacer(modifier = Modifier.width(Dimensions.smallestPadding))
                    Button(
                        onClick = {},
                        modifier = Modifier
                            .size(Dimensions.LargeIconSize)
                            .clip(RoundedCornerShape(Dimensions.SmallCornerRadius)),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Siguiente",
                            modifier = Modifier
                                .size(Dimensions.MediumIconSize)
                                .graphicsLayer(scaleX = -1f)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(Dimensions.smallestPadding))


            }

        }
        items(6) { index ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimensions.MediumPadding)
            ) {
                PerformWorkoutListComponent()
            }
            Spacer(modifier = Modifier.height(Dimensions.SmallPadding))
        }
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Button(onClick = {}) {
                    Text(text = "Terminar entrenamiento")
                }
            }
        }
    }
}

@Composable
fun PerformWorkoutListComponent() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Dimensions.MediumCornerRadius))
            .border(
                width = 2.dp,
                color = greyMed,
                shape = RoundedCornerShape(Dimensions.MediumCornerRadius)
            )
            .padding(Dimensions.MediumPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Ejercicio 1",
                color = Color.White,
                fontSize = Dimensions.LargeTextSize,
            )
            Text(
                text = "4 sets ~ 12 reps",
                color = greenLess,
                fontSize = Dimensions.SmallTextSize,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {},
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.chevron_right),
                contentDescription = "Motrar ejercicio",
                modifier = Modifier
                    .size(Dimensions.LargeIconSize)
            )
        }
    }
}

@Preview
@Composable
fun PerformWorkoutScreenPreview() {
    PerformWorkoutScreen()

    //PerformWorkoutListComponent()
}