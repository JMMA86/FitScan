package icesi.edu.co.fitscan.features.workout.ui.model

// Represents the current exercise
data class CurrentExercise(
    val name: String = "Pull up",
    val time: String = "10:32",
    val series: String = "2 de 4",
    val remainingTime: String = "Quedan 55 segundos"
)

// represents the next exercise
data class NextExercise(
    val name: String = "Nombre del ejercicio",
    val sets: Int = 4,
    val reps: Int = 12
)

// Represents the remaining exercises
data class RemainingExercise(
    val title: String,
    val sets: String,
    val reps: String
)

// Global state of the workout screen
data class WorkoutUiState(
    val title: String = "Fuerza de todo el cuerpo",
    val subtitle: String = "Subtítulos",
    val progress: String = "3/8 ejercicios completados",
    val currentExercise: CurrentExercise = CurrentExercise(),
    val nextExercise: NextExercise = NextExercise(),
    val remainingExercises: List<RemainingExercise> = List(6) { index ->
        RemainingExercise("Ejercicio $index", "3", "10")
    }
)