package icesi.edu.co.fitscan.domain.model

import com.google.gson.annotations.SerializedName
import icesi.edu.co.fitscan.features.openai.data.dto.ApiError
import icesi.edu.co.fitscan.features.openai.data.dto.ChoiceDto
import icesi.edu.co.fitscan.features.openai.data.dto.UsageDto

data class ChatCompletionResponse(
    val id: String?,
    val created: Long?,
    val model: String?,
    val choices: List<ChoiceDto>?,
    val usage: UsageDto?,
    val error: ApiError? = null
)
