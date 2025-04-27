package icesi.edu.co.fitscan.features.auth.data.remote.response

data class LoginResponseData(
    val accessToken: String, //Esto es lo que devuelve postman
    val refreshToken: String,
    val expires: Long
)

data class LoginResponse(
    val data: LoginResponseData?

)