package icesi.edu.co.fitscan.features.common.data.remote

import icesi.edu.co.fitscan.features.statistics.data.remote.ExerciseStatisticsRemoteDataSource
import okhttp3.Interceptor
import icesi.edu.co.fitscan.features.auth.data.remote.AuthRepository
import icesi.edu.co.fitscan.features.common.ui.viewmodel.AppState
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private const val BASE_URL = "http://10.0.2.2:8055"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Interceptor para añadir token de autenticación a todas las solicitudes
    private val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()

        // Obtener el token desde AppState
        val token = AppState.token

        Log.d("AUTH_TOKEN", "Token utilizado: ${token ?: "NINGUNO"}")

        // Si hay un token disponible, añadirlo como header
        val request = if (!token.isNullOrBlank()) {
            val authHeader = "Bearer $token"
            Log.d("AUTH_HEADER", "Authorization: $authHeader")

            originalRequest.newBuilder()
                .header("Authorization", authHeader)
                .build()
        } else {
            Log.d("AUTH_HEADER", "No se añadió header de autorización")
            originalRequest
        }

        chain.proceed(request)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(authInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authRepository: AuthRepository by lazy {
        retrofit.create(AuthRepository::class.java)
    }
    
    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }

    val statisticsRepository: ExerciseStatisticsRemoteDataSource by lazy {
        retrofit.create(ExerciseStatisticsRemoteDataSource::class.java)
    }
}