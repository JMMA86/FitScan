package icesi.edu.co.fitscan.features.statistics.data.mapper

import icesi.edu.co.fitscan.domain.model.ExerciseItem
import icesi.edu.co.fitscan.features.statistics.data.dto.ExerciseItemDto

object ExerciseItemMapper {
    fun toDomain(dto: ExerciseItemDto): ExerciseItem = ExerciseItem(
        id = dto.id,
        name = dto.name
    )
}
