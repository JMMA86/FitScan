package icesi.edu.co.fitscan.features.statistics.data.dto

import com.google.gson.annotations.SerializedName

data class MuscleGroupProgressResponseDto(
    @SerializedName("data")
    val data: List<MuscleGroupProgressDto>
)
