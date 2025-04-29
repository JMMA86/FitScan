package icesi.edu.co.fitscan.features.auth.data.remote.request

data class LoginRequest(
    val email: String,
    val password: String,
    //val mode: String = "json" //Todavia no se si se necesita
)