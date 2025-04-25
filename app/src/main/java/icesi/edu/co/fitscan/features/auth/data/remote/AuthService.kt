package icesi.edu.co.fitscan.features.auth.data.remote

import icesi.edu.co.fitscan.features.auth.data.remote.request.LoginRequest
import icesi.edu.co.fitscan.features.auth.data.remote.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
}