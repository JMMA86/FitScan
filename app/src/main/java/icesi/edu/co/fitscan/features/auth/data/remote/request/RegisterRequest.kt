package icesi.edu.co.fitscan.features.auth.data.remote.request

data class RegisterRequest(
    val email: String,
    val password: String,
    val firsName: String,
    val lastName: String,
)

data class User(
    val id: String? = null,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String
)

data class Customer(
    val id: String? = null,
    val userId: String, // Foreign key to the user table
    val otherField: String
)