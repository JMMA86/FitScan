package icesi.edu.co.fitscan.features.workout.data.model

import com.google.gson.annotations.SerializedName

// Modelo para un ejercicio
// Ajusta los campos según la respuesta real de Directus

data class Exercise(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("muscle_groups") val muscleGroups: String?
)

// Modelo para una rutina (workout)
data class Workout(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("duration_minutes") val durationMinutes: Int?,
    @SerializedName("difficulty") val difficulty: String?,
    @SerializedName("date_created") val dateCreated: String?,
    @SerializedName("workout_exercise") val workoutExercises: List<WorkoutExercise>?
)

// Relación entre workout y exercise
data class WorkoutExercise(
    @SerializedName("id") val id: String,
    @SerializedName("exercise_id") val exercise: Exercise?,
    @SerializedName("sets") val sets: Int?,
    @SerializedName("reps") val reps: Int?
)
