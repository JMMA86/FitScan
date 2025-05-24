package icesi.edu.co.fitscan.features.auth.data.dto

data class LoginRequestDTO(
    val email: String,
    val password: String,
    //val mode: String = "json" //Todavia no se si se necesita
)