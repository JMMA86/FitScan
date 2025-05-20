package icesi.edu.co.fitscan.features.workout.domain.usecase

import icesi.edu.co.fitscan.features.workout.data.repositories.WorkoutExerciseRepository
import java.util.UUID

class PerformWorkoutUseCase(private val performWorkoutRepository: WorkoutExerciseRepository) {
    suspend fun getWorkout(customerId: String): Result<WorkoutUiState> {
        if (customerId.isBlank()) {
            return Result.failure(IllegalArgumentException("Customer ID cannot be null or empty"))
        }
        val customerIdUUid = try {
            UUID.fromString(customerId)
        } catch (e: IllegalArgumentException) {
            return Result.failure(IllegalArgumentException("Invalid Customer ID format"))
        }
        // Fetch workouts for the customer and map the first one to WorkoutUiState
        return performWorkoutRepository.getWorkoutExercises(customerIdUUid)
            .mapCatching { workouts ->
                val workout = workouts.firstOrNull()
                    ?: throw IllegalStateException("No workouts found for customer")
                // Map Workout (domain) to WorkoutUiState (UI)
                WorkoutUiState(
                    title = workout.name,
                    subtitle = workout.type.name,
                    progress = "0/0 ejercicios completados", // TODO: calculate real progress
                    currentExercise = CurrentExercise(
                        name = "Primer ejercicio", // TODO: map real exercise
                        time = "00:00",
                        series = "1 de 1",
                        remainingTime = ""
                    ),
                    nextExercise = NextExercise(), // TODO: map real next exercise
                    remainingExercises = listOf() // TODO: map real remaining exercises
                )
            }
    }

    suspend fun endSet(current: CurrentExercise, progress: String): Pair<CurrentExercise, String> {
        val completed = current.series.split(" ")[0].toInt() + 1
        val total = current.series.split(" ")[2]
        val updatedSeries = "$completed de $total"
        val updatedCurrent = current.copy(series = updatedSeries)
        val updatedProgress = updateProgress(progress)
        return Pair(updatedCurrent, updatedProgress)
    }

    suspend fun skipToNextExercise(remainingExercises: List<RemainingExercise>): Pair<NextExercise, List<RemainingExercise>> {
        val updatedList = remainingExercises.drop(1)
        val next = updatedList.getOrNull(0)?.let {
            NextExercise(it.title, it.sets.toInt(), it.reps.toInt())
        } ?: NextExercise()
        return Pair(next, updatedList)
    }

    private fun updateProgress(progress: String): String {
        val completed = progress.split("/")[0].toInt() + 1
        val total = progress.split("/")[1].split(" ")[0].toInt()
        return "$completed/$total ejercicios completados"
    }
}