package icesi.edu.co.fitscan.domain.model

import com.google.gson.annotations.SerializedName
import icesi.edu.co.fitscan.features.openai.data.dto.MessageDto

data class ChatCompletionRequest(
    val model: String,
    val messages: List<MessageDto>,
    val temperature: Double = 0.7,
    @SerializedName("max_tokens") val maxTokens: Int = 100
)
