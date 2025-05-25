package icesi.edu.co.fitscan.features.openai.data.remote

import com.google.gson.GsonBuilder
import icesi.edu.co.fitscan.features.openai.data.datasources.OpenAiApiDatasource
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object OpenAiApiProvider {
    private const val BASE_URL = "https://api.openai.com/"
    
    fun provideOpenAiApiService(): OpenAiApiDatasource {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
            
        val gson = GsonBuilder()
            .setLenient()
            .create()
            
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(OpenAiApiDatasource::class.java)
    }
}
