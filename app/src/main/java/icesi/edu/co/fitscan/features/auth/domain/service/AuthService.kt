package icesi.edu.co.fitscan.features.auth.domain.service

import icesi.edu.co.fitscan.features.auth.data.remote.request.BodyMeasure
import icesi.edu.co.fitscan.features.auth.data.remote.request.Customer
import icesi.edu.co.fitscan.features.auth.data.remote.response.LoginResponseData

interface AuthService {
    suspend fun login(email: String, password: String): Result<LoginResponseData>
    suspend fun register(
        email: String,
        password: String,
        firsName: String,
        lastName: String
    ): Result<Unit>

    suspend fun registerCustomer(customer: Customer): Result<Unit>
    suspend fun saveBodyMeasurements(bodyMeasure: BodyMeasure): Result<Unit>
}