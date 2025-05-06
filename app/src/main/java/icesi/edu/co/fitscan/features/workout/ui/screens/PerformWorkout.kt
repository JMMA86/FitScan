package icesi.edu.co.fitscan.features.workout.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import icesi.edu.co.fitscan.R

@Composable
fun PerformWorkoutScreen(modifier: Modifier = Modifier) {
    // todo add the scrollable modifier to the column
    Column(modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White,
                            Color.Black
                        )
                    )
                )
        ) {
            Text(text = "Hello")
            Text(text = "Subtittle")
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Green)
                    .padding(4.dp)
            ) {
                Text(text = "3/8 exercises completed")
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White,
                            Color.Black,
                        )
                    )
                )
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        Color.Gray
                    )
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .wrapContentSize()
                        .fillMaxWidth()
                ) {
                    Text(text = "Exercise 1")
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = "10:32")
                }
                Text(text = "3 sets of 10 reps")
                Text(text = "Rest 60 seconds")
            }
        }

        Text(text = "Siguiente")
        Text(text = "Nombre del ejercicio")
        Row {
            Text(text = "4 sets")
            Text(text = " | ")
            Text(text = "10 reps")
        }
        Text(text = "button")
        Row {
            Text(text = "button")
            Text(text = "button")
            Text(text = "button")
        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(10) { index ->
                Text(
                    text = "Elemento $index",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(Color.White),
                    color = Color.Black
                )
            }
        }
        Text(text = "Terminar entrenamiento")
    }
}

@Composable
fun PerformWorkoutListComponent() {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Gray)
            .padding(8.dp)) {
        Column (
            modifier = Modifier,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Ejercicio 1")
            Row {
                Text(text = "4 sets ~ 12 reps")
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_run),
                contentDescription = "Run",
                modifier = Modifier
                    .padding(8.dp)
                    .background(Color.White)
                    .border(
                        width = 2.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp)
            )
        }
    }
}

@Preview
@Composable
fun PerformWorkoutScreenPreview() {
    //PerformWorkoutScreen()

    PerformWorkoutListComponent()
}