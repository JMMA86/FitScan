package icesi.edu.co.fitscan.features.workout.ui.util

import android.util.Log
import icesi.edu.co.fitscan.features.workout.data.remote.UnsplashClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ExerciseImageProvider {
    
    private val unsplashRepository = UnsplashClient.UnsplashRepository()
    
    // Cache para evitar llamadas repetidas
    private val imageCache = mutableMapOf<String, String>()
    
    /**
     * Obtiene una URL de imagen usando la API oficial de Unsplash
     */
    suspend fun getExerciseImageUrl(exerciseName: String, muscleGroups: String? = null): String? {
        val cacheKey = "${exerciseName}_${muscleGroups ?: "none"}"
        
        // Verificar cache primero
        imageCache[cacheKey]?.let { cachedUrl ->
            Log.d("ExerciseImageProvider", "🗂️ Using cached image for '$exerciseName': $cachedUrl")
            return cachedUrl
        }
        
        return withContext(Dispatchers.IO) {
            try {
                Log.d("ExerciseImageProvider", "🌐 Fetching image for '$exerciseName' with muscles: $muscleGroups")
                
                val imageUrl = unsplashRepository.searchExercisePhotos(exerciseName, muscleGroups)
                
                if (imageUrl != null) {
                    // Guardar en cache
                    imageCache[cacheKey] = imageUrl
                    Log.i("ExerciseImageProvider", "✅ Successfully got image for '$exerciseName': $imageUrl")
                    imageUrl
                } else {
                    Log.w("ExerciseImageProvider", "⚠️ No image found for '$exerciseName', trying fallback")
                    getFallbackImageUrl(exerciseName, muscleGroups)
                }
            } catch (e: Exception) {
                Log.e("ExerciseImageProvider", "🚨 Error getting image for '$exerciseName': ${e.message}", e)
                getFallbackImageUrl(exerciseName, muscleGroups)
            }
        }
    }
    
    /**
     * Obtiene una imagen de fallback cuando la API principal falla
     */
    private suspend fun getFallbackImageUrl(exerciseName: String, muscleGroups: String?): String? {
        return withContext(Dispatchers.IO) {
            try {
                // Intentar solo con el grupo muscular
                val primaryMuscle = muscleGroups?.split(",")?.firstOrNull()?.trim()
                if (!primaryMuscle.isNullOrBlank()) {
                    Log.d("ExerciseImageProvider", "🔄 Trying fallback with muscle group: '$primaryMuscle'")
                    val fallbackUrl = unsplashRepository.searchExercisePhotos("workout", primaryMuscle)
                    if (fallbackUrl != null) {
                        Log.i("ExerciseImageProvider", "✅ Fallback successful for '$exerciseName': $fallbackUrl")
                        return@withContext fallbackUrl
                    }
                }
                
                // Fallback final: búsqueda genérica de fitness
                Log.d("ExerciseImageProvider", "🏃 Using generic fitness fallback for '$exerciseName'")
                unsplashRepository.searchExercisePhotos("fitness workout", null)
                
            } catch (e: Exception) {
                Log.e("ExerciseImageProvider", "🚨 Fallback failed for '$exerciseName': ${e.message}", e)
                null
            }
        }
    }
    
    /**
     * URL de imagen por defecto (placeholder local)
     */
    fun getDefaultImagePlaceholder(): String {
        return "android.resource://icesi.edu.co.fitscan/drawable/ic_fitness_center" // Placeholder local
    }
    
    /**
     * Limpia el cache de imágenes
     */
    fun clearCache() {
        imageCache.clear()
        Log.d("ExerciseImageProvider", "🗑️ Image cache cleared")
    }
    
    /**
     * Genera una URL de imagen usando Unsplash basada en el nombre del ejercicio
     */
    fun getExerciseImageUrl(exerciseName: String, width: Int = 400, height: Int = 300): String {
        val searchQuery = cleanExerciseName(exerciseName)
        return "https://source.unsplash.com/${width}x${height}/?$searchQuery,fitness,exercise,gym"
    }
    
    /**
     * FUNCIONES SÍNCRONAS DE FALLBACK (usando placeholders)
     * Estas se usan cuando la API no está disponible o como fallback inmediato
     */
    
    /**
     * Genera una URL de imagen usando placeholders cuando la API no está disponible
     */
    fun getTranslatedExerciseImageUrl(exerciseName: String, width: Int = 400, height: Int = 300): String {
        Log.d("ExerciseImageProvider", "🔍 Generating placeholder for '$exerciseName' (${width}x${height})")
        
        val cleanName = cleanExerciseName(exerciseName)
        val translatedName = exerciseTranslations[cleanName] ?: cleanName
        
        // Por ahora usar un placeholder hasta que tengamos la API key
        val url = "https://via.placeholder.com/${width}x${height}/2196F3/FFFFFF?text=${translatedName.replace("+", "%20")}"
        
        Log.d("ExerciseImageProvider", "🎯 Generated placeholder URL for '$exerciseName': $url")
        return url
    }
    
    /**
     * Limpia el nombre del ejercicio para usar como query de búsqueda
     */
    private fun cleanExerciseName(name: String): String {
        return name.lowercase()
            .replace("á", "a")
            .replace("é", "e") 
            .replace("í", "i")
            .replace("ó", "o")
            .replace("ú", "u")
            .replace("ñ", "n")
            .replace(Regex("[^a-z0-9\\s]"), "")
            .replace("\\s+".toRegex(), "+")
    }
    
    /**
     * Mapeo de nombres en español a términos en inglés para mejor búsqueda
     */
    private val exerciseTranslations = mapOf(
        // Ejercicios básicos
        "flexiones" to "pushups",
        "sentadillas" to "squats", 
        "abdominales" to "crunches",
        "dominadas" to "pullups",
        "press de banca" to "bench+press",
        "peso muerto" to "deadlift",
        "curl de biceps" to "bicep+curl",
        "extensiones de triceps" to "tricep+extension",
        "plancha" to "plank",
        "burpees" to "burpees",
        "fondos" to "dips",
        "remo" to "rowing",
        "prensa de piernas" to "leg+press",
        "elevaciones laterales" to "lateral+raise",
        
        // Ejercicios de pecho
        "press inclinado" to "incline+press",
        "aperturas" to "chest+fly",
        "cruces" to "crossover",
        
        // Ejercicios de espalda
        "jalones" to "lat+pulldown",
        "remo con barra" to "barbell+row",
        "remo con mancuerna" to "dumbbell+row",
        "encogimientos" to "shrugs",
        
        // Ejercicios de piernas
        "extensiones de cuadriceps" to "leg+extension",
        "curl de femoral" to "leg+curl",
        "pantorrillas" to "calf+raise",
        "estocadas" to "lunges",
        "zancadas" to "lunges",
        "hip thrust" to "hip+thrust",
        
        // Ejercicios de hombros
        "press militar" to "military+press",
        "elevaciones frontales" to "front+raise",
        "elevaciones posteriores" to "rear+delt+fly",
        "vuelos posteriores" to "reverse+fly",
        
        // Ejercicios de brazos
        "martillo" to "hammer+curl",
        "curl concentrado" to "concentration+curl",
        "extensiones por encima" to "overhead+extension",
        "patadas de triceps" to "tricep+kickback"
    )
    
    /**
     * URL de imagen por defecto cuando no se encuentra una específica
     */
    fun getDefaultExerciseImageUrl(width: Int = 400, height: Int = 300): String {
        val url = "https://via.placeholder.com/${width}x${height}/FF9800/FFFFFF?text=Exercise"
        Log.d("ExerciseImageProvider", "🏃 Generated default exercise URL: $url")
        return url
    }
    
    /**
     * Genera URL de imagen basada en el grupo muscular principal
     */
    fun getImageByMuscleGroup(muscleGroups: String?): String {
        val primaryMuscle = muscleGroups?.split(",")?.firstOrNull()?.trim()?.lowercase()
        
        val muscleImageMap = mapOf(
            // Grupos principales
            "pecho" to "chest+workout",
            "espalda" to "back+workout", 
            "piernas" to "leg+workout",
            "brazos" to "arm+workout",
            "hombros" to "shoulder+workout",
            "abdomen" to "core+workout",
            "biceps" to "bicep+workout",
            "triceps" to "tricep+workout",
            "pantorrillas" to "calf+workout",
            
            // Músculos específicos
            "cuadriceps" to "quadriceps+workout",
            "femorales" to "hamstring+workout",
            "glúteos" to "glute+workout",
            "deltoides" to "shoulder+workout",
            "dorsal" to "lat+workout",
            "trapecio" to "trap+workout",
            "serrato" to "serratus+workout",
            
            // Variaciones en inglés
            "chest" to "chest+workout",
            "back" to "back+workout",
            "legs" to "leg+workout",
            "arms" to "arm+workout",
            "shoulders" to "shoulder+workout",
            "core" to "core+workout",
            "abs" to "core+workout"
        )
        
        val searchTerm = muscleImageMap[primaryMuscle] ?: "fitness+workout"
        val url = "https://via.placeholder.com/400x300/4CAF50/FFFFFF?text=${searchTerm.replace("+", "%20")}"
        
        Log.d("ExerciseImageProvider", "💪 Generated muscle group URL for '$primaryMuscle': $url")
        return url
    }
    
    /**
     * Función inteligente que combina nombre del ejercicio y grupo muscular
     * Primero intenta con la API real, luego con fallbacks
     */
    suspend fun getSmartExerciseImageUrl(exerciseName: String, muscleGroups: String? = null, width: Int = 400, height: Int = 300): String {
        Log.d("ExerciseImageProvider", "🧠 Smart search started for '$exerciseName' with muscles: $muscleGroups")
        
        // Intentar primero con la API real
        val apiUrl = getExerciseImageUrl(exerciseName, muscleGroups)
        if (apiUrl != null) {
            Log.i("ExerciseImageProvider", "🎯 Smart search: Using API result for '$exerciseName': $apiUrl")
            return apiUrl
        }
        
        // Fallback a imagen traducida
        val fallbackUrl = getTranslatedExerciseImageUrl(exerciseName, width, height)
        Log.d("ExerciseImageProvider", "🔄 Smart search: Using translated fallback for '$exerciseName': $fallbackUrl")
        return fallbackUrl
    }
    
    /**
     * Versión síncrona simplificada para uso en componentes que no pueden usar LaunchedEffect
     */
    fun getSmartExerciseImageUrlSync(exerciseName: String, muscleGroups: String? = null, width: Int = 400, height: Int = 300): String {
        Log.d("ExerciseImageProvider", "🔄 Using sync fallback for '$exerciseName'")
        
        // Primero intentar con traducción
        val translatedUrl = getTranslatedExerciseImageUrl(exerciseName, width, height)
        
        // Si hay grupos musculares disponibles, usar eso como fallback
        return if (!muscleGroups.isNullOrBlank()) {
            Log.d("ExerciseImageProvider", "💪 Using muscle group fallback for '$exerciseName': $muscleGroups")
            getImageByMuscleGroup(muscleGroups)
        } else {
            translatedUrl
        }
    }
}
