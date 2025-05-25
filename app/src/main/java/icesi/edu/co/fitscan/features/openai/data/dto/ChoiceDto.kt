package icesi.edu.co.fitscan.features.openai.data.dto

import com.google.gson.annotations.SerializedName

data class ChoiceDto(
    @SerializedName("index") val index: Int?,
    @SerializedName("message") val message: MessageDto?,
    @SerializedName("finish_reason") val finishReason: String?
)