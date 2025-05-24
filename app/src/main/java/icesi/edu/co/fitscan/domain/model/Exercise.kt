package icesi.edu.co.fitscan.domain.model

import java.util.UUID

data class Exercise(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val description: String?,
    val muscleGroups: String?
) 