package icesi.edu.co.fitscan.features.workout.domain.data.remote.response

data class Exercise(
    val name: String,
    val description: String,
    val muscleGroup: String,
)

data class WorkoutExercise(
    val workoutId: String,
    val exerciseId: String,
    val sets: Int,
    val reps: Int,
)

data class Workout(
    val id: String,
    val customer_id: String,
    val name: String,
    val type: String,
    val duration_minutes: Int,
    val difficulty: String,
    val date_created: String
)

class WorkoutExerciseResponse(
    val data: WorkoutExercise?
)

class WorkoutResponse(
    val data: List<Workout>
)