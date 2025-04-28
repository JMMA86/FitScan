@file:Suppress("PropertyName")

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
    val phone: String
)

data class BodyMeasure(
    val id: String? = null,
    val height_cm: Double,
    val weight_kg: Double,
    val arms_cm: Double,
    val chest_cm: Double,
    val waist_cm: Double,
    val hips_cm: Double,
    val thighs_cm: Double,
    val calves_cm: Double,
)