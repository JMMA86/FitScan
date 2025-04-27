package icesi.edu.co.fitscan.features.auth.data.remote.request


data class User(
    val email: String,
    val password: String,
    val first_name: String,
    val last_name: String
)

data class Customer(
    val id: String? = null,
    val user_id: String, // Foreign key to the user table
    val age: Int,
    val phone_number: String
)