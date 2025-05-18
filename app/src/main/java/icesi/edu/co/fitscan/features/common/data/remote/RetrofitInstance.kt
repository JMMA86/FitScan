package icesi.edu.co.fitscan.features.common.data.remote

import icesi.edu.co.fitscan.features.auth.data.remote.AuthRepository
import icesi.edu.co.fitscan.features.workout.data.remote.WorkoutService
import icesi.edu.co.fitscan.features.workout.data.remote.ExerciseService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    // Cambia esta URL por la de tu servidor (ip local o dominio)
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

    val workoutService: WorkoutService by lazy {
        retrofit.create(WorkoutService::class.java)
    }

    val exerciseService: ExerciseService by lazy {
        retrofit.create(ExerciseService::class.java)
    }
}