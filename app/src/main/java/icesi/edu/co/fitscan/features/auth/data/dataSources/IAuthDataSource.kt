package icesi.edu.co.fitscan.features.auth.data.dataSources

import icesi.edu.co.fitscan.features.auth.data.dto.BodyMeasure
import icesi.edu.co.fitscan.features.auth.data.dto.BodyMeasureResponseData
import icesi.edu.co.fitscan.features.auth.data.dto.Customer
import icesi.edu.co.fitscan.features.auth.data.dto.CustomerRelationated
import icesi.edu.co.fitscan.features.auth.data.dto.CustomerResponse
import icesi.edu.co.fitscan.features.auth.data.dto.CustomerResponseData
import icesi.edu.co.fitscan.features.auth.data.dto.LoginRequestDTO
import icesi.edu.co.fitscan.features.auth.data.dto.LoginResponse
import icesi.edu.co.fitscan.features.auth.data.dto.User
import icesi.edu.co.fitscan.features.auth.data.dto.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface IAuthDataSource {
    @POST("/auth/login")
    suspend fun login(@Body loginRequestDTO: LoginRequestDTO): Response<LoginResponse>

    @POST("/users/register")
    suspend fun register(@Body registerRequest: User): Response<Unit>

    @POST("/items/customer")
    suspend fun registerCustomer(
        @Body customerRequest: Customer,
        @Header("Authorization") token: String
    ): Response<CustomerResponseData>

    @POST("/items/body_measure")
    suspend fun saveBodyMeasurements(
        @Body bodyMeasure: BodyMeasure,
        @Header("Authorization") token: String
    ): Response<BodyMeasureResponseData>

    @PATCH("/items/customer/{customerId}")
    suspend fun updateCustomer(
        @Body customer: CustomerRelationated,
        @Header("Authorization") token: String,
        @Path("customerId") customerId: String
    ): Response<Unit>

    @GET("users/me")
    suspend fun getCurrentUser(@Header("Authorization") authHeader: String): Response<UserResponse>

    @GET("items/customer")
    suspend fun getCustomerByUserId(
        @Header("Authorization") authHeader: String,
        @Query("filter[user_id][_eq]") userId: String
    ): Response<CustomerResponse>
}