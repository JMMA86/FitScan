package icesi.edu.co.fitscan.features.workout.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PerformWorkoutScreen() {
    // todo add the scrollable modifier to the column
    Column(modifier = Modifier.fillMaxWidth()) {
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
            Text(text = "3/8 exercises completed")
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Black)
                .padding(8.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
            Box(
                modifier = Modifier
                    .background(color = Color.Gray)
                    .fillMaxWidth()
            ) {
                Column {
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

@Preview
@Composable
fun PerformWorkoutScreenPreview() {
    PerformWorkoutScreen()
}