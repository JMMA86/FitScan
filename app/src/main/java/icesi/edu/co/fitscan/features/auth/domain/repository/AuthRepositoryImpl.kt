package icesi.edu.co.fitscan.features.auth.domain.repository

import icesi.edu.co.fitscan.features.auth.data.remote.AuthService
import icesi.edu.co.fitscan.features.auth.data.remote.request.LoginRequest
import icesi.edu.co.fitscan.features.auth.data.remote.response.LoginResponseData
import icesi.edu.co.fitscan.features.auth.domain.repository.AuthRepository
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
}