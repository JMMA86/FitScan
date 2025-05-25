package icesi.edu.co.fitscan.features.auth.data.remote.response

data class BodyMeasureResponseData(
    val data: BodyMeasureData?
)

data class BodyMeasureData(
    val id: String?,
    val height_cm: Double?,
    val weight_kg: Double?,
    val arms_cm: Double?,
    val chest_cm: Double?,
    val waist_cm: Double?,
    val hips_cm: Double?,
    val thighs_cm: Double?,
    val calves_cm: Double?
)
