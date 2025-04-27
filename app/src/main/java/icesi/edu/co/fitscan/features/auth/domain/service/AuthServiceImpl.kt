package icesi.edu.co.fitscan.features.auth.domain.service

import android.app.Application
import android.content.Context
import android.util.Log
import icesi.edu.co.fitscan.features.auth.data.remote.AuthRepository
import icesi.edu.co.fitscan.features.auth.data.remote.request.Customer
import icesi.edu.co.fitscan.features.auth.data.remote.request.LoginRequest
import icesi.edu.co.fitscan.features.auth.data.remote.request.User
import icesi.edu.co.fitscan.features.auth.data.remote.response.LoginResponseData
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
                        Log.d(
                            "AuthServiceImpl",
                            "Token saved: ${response.body()?.data?.access_token}"
                        )
                    }
                    Result.success(response.body()!!.data!!)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Error desconocido"
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

                if (token == null) {
                    return@withContext Result.failure(Exception("User not authenticated"))
                }

                // Make the request with the token in the header
                val response = authRepository.registerCustomer(
                    customer,
                    "Bearer $token"
                )

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
}