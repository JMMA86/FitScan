package icesi.edu.co.fitscan.features.workout.data.remote

import android.util.Log
import icesi.edu.co.fitscan.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object UnsplashClient {
    
    // API Key desde BuildConfig (archivo .env)
    private val ACCESS_KEY = BuildConfig.UNSPLASH_ACCESS_KEY
    private val BASE_URL = BuildConfig.UNSPLASH_BASE_URL
    
    init {
        Log.d("UnsplashClient", "🔑 Initialized with BASE_URL: $BASE_URL")
        Log.d("UnsplashClient", "🔑 API Key loaded: ${if (ACCESS_KEY != "DEMO-KEY") "✅ Real key" else "⚠️ Demo key"}")
    }
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    val apiService: UnsplashApiService = retrofit.create(UnsplashApiService::class.java)
    
    /**
     * Repository para manejar las llamadas a la API de Unsplash
     */
    class UnsplashRepository {
        
        suspend fun searchExercisePhotos(exerciseName: String, muscleGroup: String? = null): String? {
            return try {
                // Construir query de búsqueda
                val query = buildSearchQuery(exerciseName, muscleGroup)
                
                Log.d("UnsplashRepository", "🔍 Searching for: '$query'")
                
                val response = apiService.searchPhotos(
                    authorization = "Client-ID $ACCESS_KEY",
                    query = query,
                    page = 1,
                    perPage = 5,
                    orientation = "landscape"
                )
                
                if (response.isSuccessful) {
                    val searchResponse = response.body()
                    val photos = searchResponse?.results
                    
                    if (!photos.isNullOrEmpty()) {
                        val selectedPhoto = photos.first()
                        val imageUrl = selectedPhoto.urls.small
                        
                        Log.i("UnsplashRepository", "✅ Found image for '$exerciseName': $imageUrl")
                        Log.d("UnsplashRepository", "📊 Total results: ${searchResponse.total}, Selected photo ID: ${selectedPhoto.id}")
                        
                        return imageUrl
                    } else {
                        Log.w("UnsplashRepository", "📭 No photos found for query: '$query'")
                        return null
                    }
                } else {
                    Log.e("UnsplashRepository", "❌ API Error: ${response.code()} - ${response.message()}")
                    return null
                }
                
            } catch (e: Exception) {
                Log.e("UnsplashRepository", "🚨 Exception searching for '$exerciseName': ${e.message}", e)
                null
            }
        }
        
        private fun buildSearchQuery(exerciseName: String, muscleGroup: String?): String {
            val cleanName = exerciseName.lowercase()
                .replace("á", "a").replace("é", "e").replace("í", "i")
                .replace("ó", "o").replace("ú", "u").replace("ñ", "n")
                .replace(Regex("[^a-z0-9\\s]"), "")
                .trim()
            
            // Traducir ejercicios comunes
            val translatedName = translateExerciseName(cleanName)
            
            // Combinar con grupo muscular si está disponible
            val muscleTranslation = muscleGroup?.let { translateMuscleGroup(it) }
            
            return when {
                muscleTranslation != null -> "$translatedName $muscleTranslation exercise fitness"
                else -> "$translatedName exercise fitness workout"
            }
        }
        
        private fun translateExerciseName(exerciseName: String): String {
            val translations = mapOf(
                "flexiones" to "pushups",
                "sentadillas" to "squats",
                "abdominales" to "crunches",
                "dominadas" to "pullups",
                "press de banca" to "bench press",
                "peso muerto" to "deadlift",
                "curl de biceps" to "bicep curl",
                "extensiones de triceps" to "tricep extension",
                "plancha" to "plank",
                "burpees" to "burpees",
                "fondos" to "dips",
                "remo" to "rowing",
                "prensa de piernas" to "leg press",
                "elevaciones laterales" to "lateral raise"
            )
            
            return translations[exerciseName] ?: exerciseName
        }
        
        private fun translateMuscleGroup(muscleGroup: String): String {
            val muscleTranslations = mapOf(
                "pecho" to "chest",
                "espalda" to "back",
                "piernas" to "legs",
                "brazos" to "arms",
                "hombros" to "shoulders",
                "abdomen" to "core",
                "biceps" to "biceps",
                "triceps" to "triceps"
            )
            
            return muscleTranslations[muscleGroup.lowercase()] ?: muscleGroup
        }
    }
}
