package icesi.edu.co.fitscan.domain.model

import java.util.UUID

data class MuscleGroup(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val description: String = ""
)
