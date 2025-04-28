@file:Suppress("PropertyName")

package icesi.edu.co.fitscan.features.auth.data.remote.response

data class LoginResponseData(
    val access_token: String, //Esto es lo que devuelve postman
    val refresh_token: String,
    val expires: Long
)

data class LoginResponse(
    val data: LoginResponseData?
)