package icesi.edu.co.fitscan.features.auth.domain.repository

import icesi.edu.co.fitscan.features.auth.data.remote.response.LoginResponseData

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<LoginResponseData>
}