package icesi.edu.co.fitscan.features.statistics.data.mapper

import icesi.edu.co.fitscan.domain.model.MuscleGroup
import icesi.edu.co.fitscan.features.statistics.data.dto.MuscleGroupDto
import java.util.UUID

object MuscleGroupMapper {
    fun toDomain(dto: MuscleGroupDto): MuscleGroup = MuscleGroup(
        id = UUID.fromString(dto.id),
        name = dto.name,
        description = dto.description ?: ""
    )
    
    fun toDto(domain: MuscleGroup): MuscleGroupDto = MuscleGroupDto(
        id = domain.id.toString(),
        name = domain.name,
        description = domain.description.ifEmpty { null }
    )
}
