package icesi.edu.co.fitscan.features.auth.data.remote.request

data class RegisterRequest (
    val email: String,
    val password: String,
    val firsName: String,
    val lastName: String,
)