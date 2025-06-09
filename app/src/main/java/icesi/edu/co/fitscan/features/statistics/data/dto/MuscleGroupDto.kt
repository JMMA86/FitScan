package icesi.edu.co.fitscan.features.statistics.data.dto

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class MuscleGroupDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String? = null
)
