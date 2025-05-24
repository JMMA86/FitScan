package icesi.edu.co.fitscan.features.auth.data.repositories

import android.app.Application
import android.content.Context
import android.util.Log
import icesi.edu.co.fitscan.features.auth.data.dataSources.IAuthDataSource
import icesi.edu.co.fitscan.features.auth.data.dto.BodyMeasure
import icesi.edu.co.fitscan.features.auth.data.dto.Customer
import icesi.edu.co.fitscan.features.auth.data.dto.CustomerRelationated
import icesi.edu.co.fitscan.features.auth.data.dto.LoginRequestDTO
import icesi.edu.co.fitscan.features.auth.data.dto.LoginResponseData
import icesi.edu.co.fitscan.features.auth.data.dto.User
import icesi.edu.co.fitscan.features.common.ui.viewmodel.AppState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException


class AuthRepositoryImpl(
    private val IAuthDataSource: IAuthDataSource,
    private val application: Application? = null
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<LoginResponseData> {
        return withContext(Dispatchers.IO) {
            try {
                val request = LoginRequestDTO(email = email, password = password)
                val response = IAuthDataSource.login(request)

                if (response.isSuccessful && response.body()?.data != null) {
                    // Save the token to SharedPreferences
                    if (application != null) {
                        val sharedPref =
                            application.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                        with(sharedPref.edit()) {
                            putString("AUTH_TOKEN", response.body()?.data?.access_token)
                            apply()
                        }
                        Log.d(
                            "AuthServiceImpl",
                            "Token saved: ${response.body()?.data?.access_token}"
                        )

                        val userResponse =
                            IAuthDataSource.getCurrentUser("Bearer ${response.body()?.data?.access_token}")

                        if (userResponse.isSuccessful && userResponse.body()?.data != null) {
                            val userId = userResponse.body()?.data?.id
                            val customerResponse = IAuthDataSource.getCustomerByUserId(
                                "Bearer ${response.body()?.data?.access_token}",
                                userId.toString()
                            )

                            if (customerResponse.isSuccessful && customerResponse.body()?.data != null && customerResponse.body()?.data?.isNotEmpty() == true) {
                                AppState.customerId = customerResponse.body()?.data?.get(0)?.id
                            }
                        }
                    }
                    Result.success(response.body()!!.data!!)
                } else {
                    Result.failure(HttpException(response))
                }
            } catch (e: Exception) {
                Log.e("AuthServiceImpl", "Login error: ${e.message}")
                Result.failure(e)
            }
        }
    }

    override suspend fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
    ): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val request = User(
                    email = email,
                    password = password,
                    first_name = firstName,
                    last_name = lastName
                )
                val response = IAuthDataSource.register(request)

                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("AuthRepositoryImpl", "Register failed: $errorBody")
                    Result.failure(HttpException(response))
                }
            } catch (e: IOException) {
                Log.e("AuthRepositoryImpl", "Network error: ${e.message}")
                Result.failure(e)
            } catch (e: HttpException) {
                Log.e("AuthRepositoryImpl", "HTTP error: ${e.message}")
                Result.failure(e)
            } catch (e: Exception) {
                Log.e("AuthRepositoryImpl", "Unexpected error: ${e.message}")
                Result.failure(e)
            }
        }
    }

    override suspend fun registerCustomer(customer: Customer): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                // Check if application is not null
                if (application == null) {
                    return@withContext Result.failure(Exception("Application context not available"))
                }

                val sharedPref =
                    application.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                val token = sharedPref.getString("AUTH_TOKEN", null)
                    ?: return@withContext Result.failure(Exception("User not authenticated"))

                // Make the request with the token in the header
                val response = IAuthDataSource.registerCustomer(
                    customer,
                    "Bearer $token"
                )

                AppState.customerId = response.body()?.data?.id

                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("AuthServiceImpl", "Register customer failed: $errorBody")
                    Result.failure(HttpException(response))
                }
            } catch (e: Exception) {
                Log.e("AuthServiceImpl", "Customer registration error: ${e.message}")
                Result.failure(e)
            }
        }
    }

    override suspend fun saveBodyMeasurements(bodyMeasure: BodyMeasure): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                if (application == null) {
                    return@withContext Result.failure(Exception("Application context not available"))
                }

                val sharedPref =
                    application.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                val token = sharedPref.getString("AUTH_TOKEN", null)
                    ?: return@withContext Result.failure(Exception("User not authenticated"))

                AppState.setAuthToken(token)

                Log.d("AuthServiceImpl", "Saving body measurements: $bodyMeasure")

                val response = IAuthDataSource.saveBodyMeasurements(
                    bodyMeasure,
                    "Bearer $token"
                )

                if (response.isSuccessful) {
                    val customer = CustomerRelationated(
                        body_measure_id = response.body()?.data?.id ?: "",
                    )
                    IAuthDataSource.updateCustomer(
                        customer, "Bearer $token",
                        AppState.customerId.toString()
                    )

                    Log.d("AuthServiceImpl", "Body measurements saved successfully")
                    Result.success(Unit)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("AuthServiceImpl", "Save body measurements failed: $errorBody")
                    Result.failure(HttpException(response))
                }
            } catch (e: Exception) {
                Log.e("AuthServiceImpl", "Save body measurements error: ${e.message}")
                Result.failure(e)
            }
        }
    }
}