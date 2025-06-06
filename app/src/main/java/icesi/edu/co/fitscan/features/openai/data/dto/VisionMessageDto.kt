package icesi.edu.co.fitscan.features.openai.data.dto

data class VisionMessageDto(
    val role: String,
    val content: List<ContentDto>
)

data class ContentDto(
    val type: String,
    val text: String? = null,
    val image_url: ImageUrlDto? = null
)

data class ImageUrlDto(
    val url: String
)
