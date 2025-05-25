package icesi.edu.co.fitscan.features.auth.domain.service

import android.app.Application
import android.content.Context
import android.util.Log
import icesi.edu.co.fitscan.features.auth.data.remote.AuthRepository
import icesi.edu.co.fitscan.features.auth.data.remote.request.BodyMeasure
import icesi.edu.co.fitscan.features.auth.data.remote.request.Customer
import icesi.edu.co.fitscan.features.auth.data.remote.request.CustomerRelationated
import icesi.edu.co.fitscan.features.auth.data.remote.request.LoginRequest
import icesi.edu.co.fitscan.features.auth.data.remote.request.User
import icesi.edu.co.fitscan.features.auth.data.remote.response.BodyMeasureData
import icesi.edu.co.fitscan.features.auth.data.remote.response.BodyMeasureResponseData
import icesi.edu.co.fitscan.features.auth.data.remote.response.LoginResponseData
import icesi.edu.co.fitscan.features.auth.data.remote.response.CustomerData
import icesi.edu.co.fitscan.features.common.ui.viewmodel.AppState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException


class AuthServiceImpl(
    private val authRepository: AuthRepository,
    private val application: Application? = null
) : AuthService {

    override suspend fun login(email: String, password: String): Result<LoginResponseData> {
        return withContext(Dispatchers.IO) {
            try {
                val request = LoginRequest(email = email, password = password)
                val response = authRepository.login(request)

                if (response.isSuccessful && response.body()?.data != null) {
                    // Save the token to SharedPreferences
                    if (application != null) {
                        val sharedPref =
                            application.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                        with(sharedPref.edit()) {
                            putString("AUTH_TOKEN", response.body()?.data?.access_token)
                            apply()
                        }
                        AppState.setAuthToken(response.body()?.data?.access_token.toString())
                        Log.d(
                            "AuthServiceImpl",
                            "Token saved: ${response.body()?.data?.access_token}"
                        )

                        val userResponse =
                            authRepository.getCurrentUser("Bearer ${response.body()?.data?.access_token}")

                        if (userResponse.isSuccessful && userResponse.body()?.data != null) {
                            val userId = userResponse.body()?.data?.id
                            val customerResponse = authRepository.getCustomerByUserId(
                                "Bearer ${response.body()?.data?.access_token}",
                                userId.toString()
                            )

                            if (customerResponse.isSuccessful && customerResponse.body()?.data != null && customerResponse.body()?.data?.isNotEmpty() == true) {
                                AppState.customerId = customerResponse.body()?.data?.get(0)?.id
                                Log.d(">>>AuthServiceImpl", "Customer ID: ${AppState.customerId}")
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
                val response = authRepository.register(request)

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
                val response = authRepository.registerCustomer(
                    customer,
                    "Bearer $token"
                )

                AppState.customerId = response.body()?.data?.id
                Log.e("AuthServiceImpl register customer", "Customer ID: ${AppState.customerId}")

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

                val response = authRepository.saveBodyMeasurements(
                    bodyMeasure,
                    "Bearer $token"
                )

                if (response.isSuccessful) {
                    val customer = CustomerRelationated(
                        body_measure_id = response.body()?.data?.id ?: "",
                    )
                    authRepository.updateCustomer(
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

    override suspend fun getBodyMeasureById(id: String): Result<BodyMeasureData> {
        return withContext(Dispatchers.IO) {
            try {
                if (application == null) {
                    return@withContext Result.failure(Exception("Application context not available"))
                }
                val sharedPref = application.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                val token = sharedPref.getString("AUTH_TOKEN", null)
                    ?: return@withContext Result.failure(Exception("User not authenticated"))
                val response = authRepository.getBodyMeasureById("Bearer $token", id)
                if (response.isSuccessful && response.body()?.data != null) {
                    Result.success(response.body()!!.data!!)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("AuthServiceImpl", "Get body measure failed: $errorBody")
                    Result.failure(HttpException(response))
                }
            } catch (e: Exception) {
                Log.e("AuthServiceImpl", "Get body measure error: ${e.message}")
                Result.failure(e)
            }
        }
    }

    override suspend fun getCustomerByUserId(userId: String): Result<CustomerData> {
        return withContext(Dispatchers.IO) {
            try {
                if (application == null) {
                    return@withContext Result.failure(Exception("Application context not available"))
                }
                val sharedPref = application.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                val token = sharedPref.getString("AUTH_TOKEN", null)
                    ?: return@withContext Result.failure(Exception("User not authenticated"))
                val response = authRepository.getCustomerByUserId("Bearer $token", userId)
                if (response.isSuccessful && response.body()?.data != null && response.body()!!.data!!.isNotEmpty()) {
                    Result.success(response.body()!!.data!![0])
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("AuthServiceImpl", "Get customer by userId failed: $errorBody")
                    Result.failure(HttpException(response))
                }
            } catch (e: Exception) {
                Log.e("AuthServiceImpl", "Get customer by userId error: ${e.message}")
                Result.failure(e)
            }
        }
    }

    override suspend fun getCustomerByCustomerId(customerId: String): Result<CustomerData> {
        return withContext(Dispatchers.IO) {
            try {
                if (application == null) {
                    return@withContext Result.failure(Exception("Application context not available"))
                }
                val sharedPref = application.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                val token = sharedPref.getString("AUTH_TOKEN", null)
                    ?: return@withContext Result.failure(Exception("User not authenticated"))
                val response = authRepository.getCustomerByCustomerId("Bearer $token", customerId)
                if (response.isSuccessful && response.body()?.data != null && response.body()!!.data!!.isNotEmpty()) {
                    Result.success(response.body()!!.data!![0])
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("AuthServiceImpl", "Get customer by customerId failed: $errorBody")
                    Result.failure(HttpException(response))
                }
            } catch (e: Exception) {
                Log.e("AuthServiceImpl", "Get customer by customerId error: ${e.message}")
                Result.failure(e)
            }
        }
    }
}