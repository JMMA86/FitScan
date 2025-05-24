package icesi.edu.co.fitscan.features.auth.data.repositories

import icesi.edu.co.fitscan.features.auth.data.dto.BodyMeasure
import icesi.edu.co.fitscan.features.auth.data.dto.Customer
import icesi.edu.co.fitscan.features.auth.data.dto.LoginResponseData

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<LoginResponseData>
    suspend fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ): Result<Unit>

    suspend fun registerCustomer(customer: Customer): Result<Unit>
    suspend fun saveBodyMeasurements(bodyMeasure: BodyMeasure): Result<Unit>
}