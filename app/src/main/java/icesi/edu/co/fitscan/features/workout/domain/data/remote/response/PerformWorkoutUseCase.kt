package icesi.edu.co.fitscan.features.workout.domain.data.remote.response

// Representa el estado actual del ejercicio en ejecución
data class CurrentExercise(
    val name: String = "Pull up",
    val time: String = "10:32",
    val series: String = "2 de 4",
    val remainingTime: String = "Quedan 55 segundos"
)

// Representa el siguiente ejercicio
data class NextExercise(
    val name: String = "Nombre del ejercicio",
    val sets: Int = 4,
    val reps: Int = 12
)

// Representa un ejercicio restante en la lista
data class RemainingExercise(
    val title: String,
    val sets: String,
    val reps: String
)

// Estado global de la pantalla
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