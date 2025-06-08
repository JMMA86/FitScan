package icesi.edu.co.fitscan.features.openai.data.remote

import com.google.gson.GsonBuilder
import icesi.edu.co.fitscan.features.openai.data.datasources.OpenAiApiDatasource
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object OpenAiApiProvider {
    private const val BASE_URL = "https://api.openai.com/"
      fun provideOpenAiApiService(): OpenAiApiDatasource {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        // Enhanced OkHttpClient for emulator compatibility
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)  // Increased timeout for emulators
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)  // Enable retry for network issues
            .build()
              val gson = GsonBuilder()
            .create()
            
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(OpenAiApiDatasource::class.java)
    }
}
