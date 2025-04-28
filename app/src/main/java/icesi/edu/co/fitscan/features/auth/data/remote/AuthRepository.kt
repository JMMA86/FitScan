package icesi.edu.co.fitscan.features.auth.data.remote

import icesi.edu.co.fitscan.features.auth.data.remote.request.BodyMeasure
import icesi.edu.co.fitscan.features.auth.data.remote.request.Customer
import icesi.edu.co.fitscan.features.auth.data.remote.request.LoginRequest
import icesi.edu.co.fitscan.features.auth.data.remote.request.User
import icesi.edu.co.fitscan.features.auth.data.remote.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthRepository {
    @POST("/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("/users/register")
    suspend fun register(@Body registerRequest: User): Response<Unit>

    @POST("/items/customer")
    suspend fun registerCustomer(
        @Body customerRequest: Customer,
        @Header("Authorization") token: String
    ): Response<Unit>

    @POST("/items/body_measure")
    suspend fun saveBodyMeasurements(
        @Body bodyMeasure: BodyMeasure,
        @Header("Authorization") token: String
    ): Response<Unit>
}