package icesi.edu.co.fitscan.features.workout.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import icesi.edu.co.fitscan.R
import icesi.edu.co.fitscan.ui.theme.Dimensions
import icesi.edu.co.fitscan.ui.theme.backgroundGrey
import icesi.edu.co.fitscan.ui.theme.greenLess
import icesi.edu.co.fitscan.ui.theme.greyButton
import icesi.edu.co.fitscan.ui.theme.greyLight
import icesi.edu.co.fitscan.ui.theme.greyMed
import icesi.edu.co.fitscan.ui.theme.greyTrueLight
import icesi.edu.co.fitscan.ui.theme.redDangerous

@Composable
fun PerformWorkoutScreen(modifier: Modifier = Modifier) {
    val title by remember { mutableStateOf("Fuerza de todo el cuerpo") }
    val subtitle by remember { mutableStateOf("Subtítulos") }
    val sets by remember { mutableStateOf("3/8 ejercicios completados") }

    val actualExerciseSubtitle by remember { mutableStateOf("Ejercicio actual") }
    val exerciseName by remember { mutableStateOf("Pull up") }
    val time by remember { mutableStateOf("10:32") }
    val series by remember { mutableStateOf("2 de 4") }
    val remainingTime by remember { mutableStateOf("Quedan 55 segundos") }

    val nextSubtitle by remember { mutableStateOf("Siguiente ejercicio") }
    val exerciseNameNext by remember { mutableStateOf("Nombre del ejercicio") }
    val setsNext by remember { mutableIntStateOf(4) }
    val repsNext by remember { mutableIntStateOf(12) }

    val endSetButtonText by remember { mutableStateOf("Terminar set") }

    LazyColumn(
        modifier
            .background(backgroundGrey)
    ) {
        item {
            // Header, subtitles and progress
            Spacer(modifier = Modifier.height(Dimensions.SmallPadding))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal=Dimensions.MediumPadding)
            ) {
                Text(
                    text = title,
                    fontSize = Dimensions.XLargeTextSize,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(Dimensions.SmallPadding))
                Text(
                    text = subtitle,
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
                        text = sets,
                        fontSize = Dimensions.MediumTextSize,
                        color = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.height(Dimensions.SmallPadding))

            // Current exercise
            Column (
                modifier = Modifier.padding(horizontal=Dimensions.MediumPadding)
            ) {
                Text(
                    text= actualExerciseSubtitle,
                    color = Color.Gray,
                    fontSize = Dimensions.SmallTextSize,
                )
                Spacer(modifier = Modifier.height(Dimensions.SmallPadding))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
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
                                text = exerciseName,
                                fontSize = Dimensions.LargeTextSize,
                                color = Color.White,
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = time,
                                fontSize = Dimensions.LargeTextSize,
                                color = greenLess,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(Dimensions.SmallPadding))
                        Text(
                            text = "Serie $series",
                            color = greenLess
                        )
                        Spacer(modifier = Modifier.height(Dimensions.SmallPadding))
                        Text(
                            text = remainingTime,
                            color = Color.White
                        )
                    }
                }
            }

            // Next exercise
            Column(
                modifier = Modifier.padding(Dimensions.MediumPadding)
            ) {
                Text(
                    text = nextSubtitle,
                    color = Color.White
                )
                Text(
                    text = exerciseNameNext,
                    color = Color.White,
                    fontSize = Dimensions.LargeTextSize,
                    fontWeight = FontWeight.Bold
                )
                Row {
                    Text(
                        text = "$setsNext sets | $repsNext reps",
                        color = Color.White,
                    )
                }

                // Workout controls
                Spacer(modifier = Modifier.height(Dimensions.smallestPadding))
                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = greenLess),
                ) {
                    Text(
                        text = endSetButtonText,
                        fontSize = Dimensions.MediumTextSize
                    )
                }
                Spacer(modifier = Modifier.height(Dimensions.smallestPadding))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {},
                        shape = RectangleShape,
                        modifier = Modifier
                            .size(Dimensions.LargeIconSize)
                            .clip(RoundedCornerShape(Dimensions.MediumCornerRadius)),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = greyButton),
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Anterior",
                            modifier = Modifier.size(Dimensions.MediumIconSize),
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Button(
                        onClick = {},
                        shape = RectangleShape,
                        modifier = Modifier
                            .size(Dimensions.LargeIconSize)
                            .clip(RoundedCornerShape(Dimensions.MediumCornerRadius)),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = greyButton),
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.pause_outline_filled),
                            contentDescription = "Detener workout",
                            modifier = Modifier
                                .size(Dimensions.MediumIconSize)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Button(
                        onClick = {},
                        shape = RectangleShape,
                        modifier = Modifier
                            .size(Dimensions.LargeIconSize)
                            .clip(RoundedCornerShape(Dimensions.MediumCornerRadius)),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = greyButton),
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

        // Remaining exercises list
        items(6) { index ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimensions.MediumPadding),
            ) {
                PerformWorkoutListComponent("Ejercicio $index", "3", "10")
            }
            Spacer(modifier = Modifier.height(Dimensions.SmallPadding))
        }

        // Finish workout button
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Button(
                    onClick = {},
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = redDangerous),
                    border = BorderStroke(2.dp, redDangerous)
                ) {
                    Text(
                        text = "Terminar entrenamiento",
                        fontSize = Dimensions.MediumTextSize,
                        color = redDangerous
                    )
                }
            }
        }
    }
}

@Composable
fun PerformWorkoutListComponent(title: String = "NA", sets: String = "NA", reps: String = "NA") {
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
                text = title,
                color = Color.White,
                fontSize = Dimensions.LargeTextSize,
            )
            Text(
                text = "$sets sets ~ $reps reps",
                color = greenLess,
                fontSize = Dimensions.SmallTextSize,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {},
            shape = RectangleShape,
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(containerColor = greyMed),
            modifier = Modifier.size(Dimensions.LargeIconSize).clip(RoundedCornerShape(Dimensions.SmallCornerRadius))
        ) {
            Icon(
                painter = painterResource(id = R.drawable.chevron_right),
                contentDescription = "Motrar ejercicio",
                tint = greenLess,
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