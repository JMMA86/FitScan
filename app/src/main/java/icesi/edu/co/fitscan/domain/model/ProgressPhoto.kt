package icesi.edu.co.fitscan.domain.model

data class ProgressPhoto(
    val id: String,
    val customerId: String,
    val photoDate: String?,
    val title: String?,
    val imagePath: String?
)
