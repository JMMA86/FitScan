package icesi.edu.co.fitscan.features.auth.domain.repository

import icesi.edu.co.fitscan.features.auth.data.remote.response.LoginResponseData

interface AuthService {
    suspend fun login(email: String, password: String): Result<LoginResponseData>
    suspend fun register(
        email: String,
        password: String,
        firsName: String,
        lastName: String
    ): Result<Unit>
}