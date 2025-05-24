@file:Suppress("PropertyName")

package icesi.edu.co.fitscan.features.auth.data.dto

data class LoginResponseData(
    val access_token: String, //Esto es lo que devuelve postman
    val refresh_token: String,
    val expires: Long
)

data class LoginResponse(
    val data: LoginResponseData?
)

data class UserResponse(
    val data: UserData?
)

data class UserData(
    val id: String,
    val first_name: String?,
    val last_name: String?,
    val email: String,
    val location: String?,
    val title: String?,
    val description: String?,
    val tags: String?,
    val avatar: String?,
    val language: String?,
    val tfa_secret: String?,
    val status: String,
    val role: String,
    val token: String?,
    val last_access: String?,
    val last_page: String?,
    val provider: String?,
    val external_identifier: String?,
    val auth_data: String?,
    val email_notifications: Boolean,
    val appearance: String?,
    val theme_dark: String?,
    val theme_light: String?,
    val theme_light_overrides: String?,
    val theme_dark_overrides: String?,
    val policies: List<Any>
)

data class CustomerResponse(
    val data: List<CustomerData>?
)

data class CustomerData(
    val id: String,
    val age: Int,
    val phone: String,
    val training_level_id: String,
    val main_goal_id: String,
    val body_measure_id: String,
    val user_id: String
)