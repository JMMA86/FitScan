package icesi.edu.co.fitscan.features.statistics.data.mapper

import icesi.edu.co.fitscan.domain.model.ProgressPhoto
import icesi.edu.co.fitscan.features.statistics.data.dto.ProgressPhotoDto

object ProgressPhotoMapper {
    fun toDomain(dto: ProgressPhotoDto): ProgressPhoto = ProgressPhoto(
        id = dto.id,
        customerId = dto.customer_id,
        photoDate = dto.photo_date,
        title = dto.title,
        imagePath = dto.image_path
    )
}
