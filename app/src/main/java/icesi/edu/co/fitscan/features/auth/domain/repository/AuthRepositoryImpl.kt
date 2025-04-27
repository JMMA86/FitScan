package icesi.edu.co.fitscan.features.auth.domain.repository

import android.util.Log
import icesi.edu.co.fitscan.features.auth.data.remote.AuthService
import icesi.edu.co.fitscan.features.auth.data.remote.request.LoginRequest
import icesi.edu.co.fitscan.features.auth.data.remote.request.RegisterRequest
import icesi.edu.co.fitscan.features.auth.data.remote.response.LoginResponseData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException


class AuthRepositoryImpl(private val authService: AuthService) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<LoginResponseData> {
        return withContext(Dispatchers.IO) {
            try {
                val request = LoginRequest(email = email, password = password)
                val response = authService.login(request)

                if (response.isSuccessful && response.body()?.data != null) {
                    Result.success(response.body()!!.data!!)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                    Result.failure(HttpException(response))
                }
            } catch (e: IOException) {
                Result.failure(e)
            } catch (e: HttpException) {
                Result.failure(e)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun register(
        email: String,
        password: String,
        firsName: String,
        lastName: String,
    ): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val request = RegisterRequest(
                    email = email,
                    password = password,
                    firsName = firsName,
                    lastName = lastName
                )
                val response = authService.register(request)

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
}