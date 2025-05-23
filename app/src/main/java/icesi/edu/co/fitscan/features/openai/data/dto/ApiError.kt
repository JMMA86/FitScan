package icesi.edu.co.fitscan.features.openai.data.dto

import com.google.gson.annotations.SerializedName

data class ApiError(
    @SerializedName("message") val message: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("param") val param: String?,
    @SerializedName("code") val code: String?
)