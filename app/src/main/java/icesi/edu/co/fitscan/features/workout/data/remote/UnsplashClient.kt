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
            
            // Traducir ejercicios comunes (prioridad principal)
            val translatedName = translateExerciseName(cleanName)
            
            // Construir query enfocado en el ejercicio específico
            val query = "$translatedName exercise gym fitness"
            
            Log.d("UnsplashRepository", "🎯 Built exercise-focused query: '$query' (from '$exerciseName')")
            return query
        }
        
        private fun translateExerciseName(exerciseName: String): String {
            val translations = mapOf(
                // Ejercicios básicos de pecho
                "flexiones" to "pushups",
                "press de banca" to "bench press",
                "press inclinado" to "incline bench press",
                "aperturas" to "chest fly",
                "cruces" to "cable crossover",
                "fondos" to "dips",
                
                // Ejercicios de piernas
                "sentadillas" to "squats",
                "prensa de piernas" to "leg press",
                "peso muerto" to "deadlift",
                "estocadas" to "lunges",
                "zancadas" to "walking lunges",
                "extensiones de cuadriceps" to "leg extension",
                "curl de femoral" to "leg curl",
                "pantorrillas" to "calf raise",
                "hip thrust" to "hip thrust",
                
                // Ejercicios de espalda
                "dominadas" to "pullups",
                "jalones" to "lat pulldown",
                "remo" to "rowing",
                "remo con barra" to "barbell row",
                "remo con mancuerna" to "dumbbell row",
                "encogimientos" to "shrugs",
                
                // Ejercicios de hombros
                "press militar" to "military press",
                "elevaciones laterales" to "lateral raise",
                "elevaciones frontales" to "front raise",
                "elevaciones posteriores" to "rear delt fly",
                "vuelos posteriores" to "reverse fly",
                
                // Ejercicios de brazos
                "curl de biceps" to "bicep curl",
                "martillo" to "hammer curl",
                "curl concentrado" to "concentration curl",
                "extensiones de triceps" to "tricep extension",
                "extensiones por encima" to "overhead tricep extension",
                "patadas de triceps" to "tricep kickback",
                
                // Ejercicios de core
                "abdominales" to "crunches",
                "plancha" to "plank",
                
                // Ejercicios funcionales
                "burpees" to "burpees",
                "thrusters" to "thrusters",
                "wall balls" to "wall ball",
                "box jumps" to "box jump",
                "mountain climbers" to "mountain climber",
                
                // Ejercicios cardiovasculares
                "correr" to "running",
                "trotar" to "jogging",
                "caminar" to "walking",
                "bicicleta" to "cycling",
                "saltar cuerda" to "jump rope",
                "eliptica" to "elliptical",
                
                // Ejercicios en inglés (por si acaso)
                "pushups" to "pushups",
                "push ups" to "pushups",
                "pull ups" to "pullups",
                "bench press" to "bench press",
                "deadlift" to "deadlift"
            )
            
            val result = translations[exerciseName] ?: exerciseName
            Log.d("UnsplashRepository", "🔄 Translated '$exerciseName' -> '$result'")
            return result
        }
    }
}
