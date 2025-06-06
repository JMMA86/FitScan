package icesi.edu.co.fitscan.domain.model

import com.google.gson.annotations.SerializedName
import icesi.edu.co.fitscan.features.openai.data.dto.VisionMessageDto

data class VisionChatCompletionRequest(
    val model: String,
    val messages: List<VisionMessageDto>,
    val temperature: Double = 0.7,
    @SerializedName("max_tokens") val maxTokens: Int = 300
)
