package icesi.edu.co.fitscan.features.auth.data.remote

import icesi.edu.co.fitscan.features.auth.data.remote.request.BodyMeasure
import icesi.edu.co.fitscan.features.auth.data.remote.request.Customer
import icesi.edu.co.fitscan.features.auth.data.remote.request.CustomerRelationated
import icesi.edu.co.fitscan.features.auth.data.remote.request.LoginRequest
import icesi.edu.co.fitscan.features.auth.data.remote.request.User
import icesi.edu.co.fitscan.features.auth.data.remote.response.BodyMeasureResponseData
import icesi.edu.co.fitscan.features.auth.data.remote.response.CustomerResponse
import icesi.edu.co.fitscan.features.auth.data.remote.response.CustomerResponseData
import icesi.edu.co.fitscan.features.auth.data.remote.response.LoginResponse
import icesi.edu.co.fitscan.features.auth.data.remote.response.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthRepository {
    @POST("/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

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
        @retrofit2.http.Path("customerId") customerId: String
    ): Response<Unit>

    @GET("users/me")
    suspend fun getCurrentUser(@Header("Authorization") authHeader: String): Response<UserResponse>

    @GET("items/customer")
    suspend fun getCustomerByUserId(
        @Header("Authorization") authHeader: String,
        @Query("filter[user_id][_eq]") userId: String
    ): Response<CustomerResponse>

    @GET("items/customer")
    suspend fun getCustomerByCustomerId(
        @Header("Authorization") authHeader: String,
        @Query("filter[id][_eq]") userId: String
    ): Response<CustomerResponse>

    @GET("items/body_measure/{id}")
    suspend fun getBodyMeasureById(
        @Header("Authorization") authHeader: String,
        @retrofit2.http.Path("id") id: String
    ): Response<BodyMeasureResponseData>
}