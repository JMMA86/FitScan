package icesi.edu.co.fitscan.features.common.data.remote

import icesi.edu.co.fitscan.features.auth.data.remote.AuthRepository
import icesi.edu.co.fitscan.features.home.domain.repository.impl.DashboardRepositoryImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private const val BASE_URL = "https://fitscan.onrender.com/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
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

    val dashboardRepository: DashboardRepositoryImpl by lazy {
        retrofit.create(DashboardRepositoryImpl::class.java)
    }
}