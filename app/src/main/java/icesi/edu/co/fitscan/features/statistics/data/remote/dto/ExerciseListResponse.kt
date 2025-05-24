package icesi.edu.co.fitscan.features.statistics.data.remote.dto

data class ExerciseListResponse(val data: List<ExerciseItem>)
data class ExerciseItem(
    val id: String,
    val name: String
)
