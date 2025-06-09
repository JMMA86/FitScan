package icesi.edu.co.fitscan.domain.model

data class MuscleGroupProgress(
    val muscleGroup: MuscleGroup,
    val progressPercentage: Float, // Value between 0.0 and 1.0
    val totalExercises: Int,
    val completedThisWeek: Int,
    val avgWeightLifted: Float = 0f,
    val totalVolume: Float = 0f // sets * reps * weight
)
