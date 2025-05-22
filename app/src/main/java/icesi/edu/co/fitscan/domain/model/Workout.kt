package icesi.edu.co.fitscan.domain.model

import java.util.UUID
import java.util.Date

data class Workout(
    val id: UUID = UUID.randomUUID(),
    val customerId: UUID,
    val name: String,
    val type: WorkoutType,
    val durationMinutes: Int?,
    val difficulty: String?,
    val dateCreated: Date = Date()
)

