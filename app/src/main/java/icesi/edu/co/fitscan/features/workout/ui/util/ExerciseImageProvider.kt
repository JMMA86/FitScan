package icesi.edu.co.fitscan.features.workout.ui.util

import android.util.Log
import androidx.compose.ui.graphics.vector.ImageVector
import icesi.edu.co.fitscan.features.workout.data.remote.UnsplashClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Sealed class para representar diferentes tipos de representaciones visuales de ejercicios
 */
sealed class ExerciseVisual {
    data class ImageUrl(val url: String) : ExerciseVisual()
    data class Icon(val icon: ImageVector) : ExerciseVisual()
}

object ExerciseImageProvider {

    private val unsplashRepository = UnsplashClient.UnsplashRepository()

    // Cache en memoria más robusto
    private val memoryCache = mutableMapOf<String, String>()
    private val failedRequests = mutableSetOf<String>() // Cache de requests fallidos

    // Counter para limitar requests de API
    private var apiRequestCount = 0
    private const val MAX_API_REQUESTS = 40 // Dejar 10 de margen

    // Cache de imágenes precargadas (las más comunes) - URLs reales de Unsplash
    private val preloadedImages = mapOf(
        // Ejercicios más populares con URLs reales
        "flexiones" to "https://images.unsplash.com/photo-1571019614242-c5c5dee9f50b?w=400&h=300&fit=crop",
        "dominadas" to "https://images.unsplash.com/photo-1605296867424-35fc25c9212a?w=400&h=300&fit=crop",
        "peso muerto" to "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=400&h=300&fit=crop",
        "curl de biceps" to "https://images.unsplash.com/photo-1581009146145-b5ef050c2e1e?w=400&h=300&fit=crop",
        "abdominales" to "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=400&h=300&fit=crop",
        "sentadillas" to "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=400&h=300&fit=crop",
        "press_de_banca" to "https://images.unsplash.com/photo-1652363722833-509b3aac287b?q=80&w=400&h=300&auto=format&fit=crop",
        "plancha" to "https://images.unsplash.com/photo-1626444231642-6bd985bca16a?q=80&w=400&h=300&auto=format&fit=crop",
        "burpees" to "https://images.unsplash.com/photo-1540472736633-28eeff4a185c?q=80&w=400&h=300&auto=format&fit=crop",
        "remo" to "https://images.unsplash.com/photo-1646072508128-c6413aaeeae6?q=80&w=400&h=300&auto=format&fit=crop",
        "fondos" to "https://images.unsplash.com/photo-1720788073779-04a9e709935c?q=80&w=400&h=300&auto=format&fit=crop",
        "elevaciones_laterales" to "https://plus.unsplash.com/premium_photo-1663045367772-c746a5c707e3?q=80&w=400&h=300&auto=format&fit=crop",
        "press_militar" to "https://plus.unsplash.com/premium_photo-1663036275037-413d2f634be7?q=80&w=400&h=300&auto=format&fit=crop",
        "jalones" to "https://plus.unsplash.com/premium_photo-1664298328578-71c5f671737a?q=80&w=400&300&auto=format&fit=crop",
        "prensa_de_piernas" to "https://images.unsplash.com/photo-1434682772747-f16d3ea162c3?q=80&w=400&h=300&auto=format&fit=crop",
    )

    init {
        Log.d(
            "ExerciseImageProvider",
            "🎯 Initialized with ${preloadedImages.size} preloaded images"
        )
        Log.d("ExerciseImageProvider", "📊 API Request limit: $MAX_API_REQUESTS")
    }

    /**
     * Obtiene una URL de imagen usando la API oficial de Unsplash
     */
    suspend fun getExerciseImageUrl(exerciseName: String, muscleGroups: String? = null): String? {
        val cacheKey = "${exerciseName}_${muscleGroups ?: "none"}"

        // 1. Verificar cache de imágenes precargadas primero
        preloadedImages[exerciseName.lowercase()]?.let { preloadedUrl ->
            Log.d(
                "ExerciseImageProvider",
                "🎯 Using preloaded image for '$exerciseName': $preloadedUrl"
            )
            return preloadedUrl
        }

        // 2. Verificar cache en memoria
        memoryCache[cacheKey]?.let { cachedUrl ->
            Log.d("ExerciseImageProvider", "🗂️ Using cached image for '$exerciseName': $cachedUrl")
            return cachedUrl
        }

        // 3. Verificar si ya falló antes
        if (failedRequests.contains(cacheKey)) {
            Log.d("ExerciseImageProvider", "❌ Skipping known failed request for '$exerciseName'")
            return null
        }

        // 4. Verificar límite de API
        if (apiRequestCount >= MAX_API_REQUESTS) {
            Log.w(
                "ExerciseImageProvider",
                "🚫 API request limit reached ($apiRequestCount/$MAX_API_REQUESTS). Skipping API call for '$exerciseName'"
            )
            return null
        }

        return withContext(Dispatchers.IO) {
            try {
                Log.d(
                    "ExerciseImageProvider",
                    "🌐 Fetching image for '$exerciseName' (API calls: $apiRequestCount/$MAX_API_REQUESTS)"
                )

                apiRequestCount++

                // Log estadísticas cada 10 requests
                if (apiRequestCount % 10 == 0) {
                    logApiStats()
                }

                val imageUrl = unsplashRepository.searchExercisePhotos(exerciseName, null)

                if (imageUrl != null) {
                    // Guardar en cache
                    memoryCache[cacheKey] = imageUrl
                    Log.i(
                        "ExerciseImageProvider",
                        "✅ Successfully got image for '$exerciseName': $imageUrl (API calls: $apiRequestCount/$MAX_API_REQUESTS)"
                    )
                    imageUrl
                } else {
                    // Marcar como fallido para evitar intentos futuros
                    failedRequests.add(cacheKey)
                    Log.w(
                        "ExerciseImageProvider",
                        "⚠️ No image found for '$exerciseName', marked as failed"
                    )
                    null
                }
            } catch (e: Exception) {
                failedRequests.add(cacheKey)
                Log.e(
                    "ExerciseImageProvider",
                    "🚨 Error getting image for '$exerciseName': ${e.message}",
                    e
                )
                null
            }
        }
    }

    /**
     * Obtiene una imagen de fallback cuando la API principal falla
     */
    private suspend fun getFallbackImageUrl(exerciseName: String, muscleGroups: String?): String? {
        return withContext(Dispatchers.IO) {
            try {
                // Intentar con variaciones del nombre del ejercicio
                Log.d("ExerciseImageProvider", "🔄 Trying fallback variations for '$exerciseName'")

                // Intentar con términos más genéricos del ejercicio
                val genericTerms = getGenericExerciseTerms(exerciseName)
                for (term in genericTerms) {
                    Log.d("ExerciseImageProvider", "🎯 Trying generic term: '$term'")
                    val fallbackUrl = unsplashRepository.searchExercisePhotos(term, null)
                    if (fallbackUrl != null) {
                        Log.i(
                            "ExerciseImageProvider",
                            "✅ Generic term successful for '$exerciseName': $fallbackUrl"
                        )
                        return@withContext fallbackUrl
                    }
                }

                // Fallback final: búsqueda genérica de fitness
                Log.d(
                    "ExerciseImageProvider",
                    "🏃 Using generic fitness fallback for '$exerciseName'"
                )
                unsplashRepository.searchExercisePhotos("gym workout", null)

            } catch (e: Exception) {
                Log.e(
                    "ExerciseImageProvider",
                    "🚨 Fallback failed for '$exerciseName': ${e.message}",
                    e
                )
                null
            }
        }
    }

    /**
     * Obtiene términos genéricos relacionados con el ejercicio
     */
    private fun getGenericExerciseTerms(exerciseName: String): List<String> {
        val name = exerciseName.lowercase()

        return when {
            // Ejercicios de empuje
            name.contains("press") || name.contains("flexiones") || name.contains("fondos") ->
                listOf("push exercise", "chest workout", "pushing")

            // Ejercicios de tracción
            name.contains("dominadas") || name.contains("remo") || name.contains("jalones") ->
                listOf("pull exercise", "back workout", "pulling")

            // Ejercicios de piernas
            name.contains("sentadillas") || name.contains("piernas") || name.contains("peso muerto") ->
                listOf("leg exercise", "squat", "lower body")

            // Ejercicios de brazos
            name.contains("curl") || name.contains("biceps") || name.contains("triceps") ->
                listOf("arm exercise", "bicep", "tricep")

            // Ejercicios de core
            name.contains("abdominales") || name.contains("plancha") || name.contains("core") ->
                listOf("core exercise", "abs workout", "plank")

            // Ejercicios cardiovasculares
            name.contains("correr") || name.contains("cardio") || name.contains("bicicleta") ->
                listOf("cardio exercise", "fitness", "training")

            else -> listOf("strength training", "fitness exercise", "gym workout")
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
        memoryCache.clear()
        failedRequests.clear()
        apiRequestCount = 0
        Log.d("ExerciseImageProvider", "🗑️ Image cache cleared, API counter reset")
    }

    /**
     * Genera una URL de imagen usando Unsplash basada en el nombre del ejercicio
     */
    fun getExerciseImageUrl(exerciseName: String, width: Int = 400, height: Int = 300): String {
        val searchQuery = cleanExerciseName(exerciseName)
        return "https://source.unsplash.com/${width}x${height}/?${searchQuery}"
    }

    /**
     * FUNCIONES SÍNCRONAS DE FALLBACK (usando placeholders)
     * Estas se usan cuando la API no está disponible o como fallback inmediato
     */

    /**
     * Genera una URL de imagen usando placeholders cuando la API no está disponible
     */
    fun getTranslatedExerciseImageUrl(
        exerciseName: String,
        width: Int = 400,
        height: Int = 300
    ): String {
        Log.d(
            "ExerciseImageProvider",
            "🔍 Generating placeholder for '$exerciseName' (${width}x${height})"
        )

        val cleanName = cleanExerciseName(exerciseName)
        val translatedName = exerciseTranslations[cleanName] ?: cleanName

        // Por ahora usar un placeholder hasta que tengamos la API key
        val url = "https://via.placeholder.com/${width}x${height}/2196F3/FFFFFF?text=${
            translatedName.replace(
                "+",
                "%20"
            )
        }"

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
        val url = "https://via.placeholder.com/400x300/4CAF50/FFFFFF?text=${
            searchTerm.replace(
                "+",
                "%20"
            )
        }"

        Log.d("ExerciseImageProvider", "💪 Generated muscle group URL for '$primaryMuscle': $url")
        return url
    }

    /**
     * Función inteligente que combina nombre del ejercicio y grupo muscular
     * Primero intenta con la API real, luego con fallbacks
     */
    suspend fun getSmartExerciseImageUrl(
        exerciseName: String,
        muscleGroups: String? = null,
        width: Int = 400,
        height: Int = 300
    ): String {
        Log.d(
            "ExerciseImageProvider",
            "🧠 Smart search started for '$exerciseName' with muscles: $muscleGroups"
        )

        // Intentar primero con la API real
        val apiUrl = getExerciseImageUrl(exerciseName, muscleGroups)
        if (apiUrl != null) {
            Log.i(
                "ExerciseImageProvider",
                "🎯 Smart search: Using API result for '$exerciseName': $apiUrl"
            )
            return apiUrl
        }

        // Fallback a imagen traducida
        val fallbackUrl = getTranslatedExerciseImageUrl(exerciseName, width, height)
        Log.d(
            "ExerciseImageProvider",
            "🔄 Smart search: Using translated fallback for '$exerciseName': $fallbackUrl"
        )
        return fallbackUrl
    }

    /**
     * Versión síncrona simplificada para uso en componentes que no pueden usar LaunchedEffect
     */
    fun getSmartExerciseImageUrlSync(
        exerciseName: String,
        muscleGroups: String? = null,
        width: Int = 400,
        height: Int = 300
    ): String {
        Log.d("ExerciseImageProvider", "🔄 Using sync fallback for '$exerciseName'")

        // Primero intentar con traducción
        val translatedUrl = getTranslatedExerciseImageUrl(exerciseName, width, height)

        // Si hay grupos musculares disponibles, usar eso como fallback
        return if (!muscleGroups.isNullOrBlank()) {
            Log.d(
                "ExerciseImageProvider",
                "💪 Using muscle group fallback for '$exerciseName': $muscleGroups"
            )
            getImageByMuscleGroup(muscleGroups)
        } else {
            translatedUrl
        }
    }

    /**
     * Obtiene estadísticas de uso de la API para monitoreo
     */
    fun getApiUsageStats(): String {
        val cacheHits = memoryCache.size
        val preloadedHits = preloadedImages.size
        val failedAttempts = failedRequests.size

        return """
            📊 API Usage Statistics:
            🌐 API Requests: $apiRequestCount/$MAX_API_REQUESTS
            💾 Cache Hits: $cacheHits
            🎯 Preloaded Images: $preloadedHits 
            ❌ Failed Requests: $failedAttempts
            📈 API Remaining: ${MAX_API_REQUESTS - apiRequestCount}
        """.trimIndent()
    }

    /**
     * Log de estadísticas de uso de API
     */
    fun logApiStats() {
        Log.i("ExerciseImageProvider", getApiUsageStats())
    }

    /**
     * Obtiene la mejor representación visual para un ejercicio (imagen o icono)
     * Esta función combina imágenes con iconos como fallback
     */
    suspend fun getExerciseVisual(
        exerciseName: String,
        muscleGroups: String? = null
    ): ExerciseVisual {
        // 1. Intentar obtener imagen primero
        val imageUrl = getExerciseImageUrl(exerciseName, muscleGroups)

        if (imageUrl != null) {
            Log.d("ExerciseImageProvider", "📸 Using image for '$exerciseName': $imageUrl")
            return ExerciseVisual.ImageUrl(imageUrl)
        }

        // 2. Fallback a icono cuando no hay imagen disponible
        val icon = ExerciseIconProvider.getBestIcon(exerciseName, muscleGroups)
        Log.d("ExerciseImageProvider", "🎯 Using icon fallback for '$exerciseName'")
        return ExerciseVisual.Icon(icon)
    }

    /**
     * Versión síncrona que prioriza iconos para mejor rendimiento
     * Útil cuando se necesita una respuesta inmediata
     */
    fun getExerciseVisualSync(exerciseName: String, muscleGroups: String? = null): ExerciseVisual {
        val cacheKey = "${exerciseName}_${muscleGroups ?: "none"}"

        // 1. Verificar cache de imágenes precargadas
        preloadedImages[exerciseName.lowercase()]?.let { preloadedUrl ->
            Log.d("ExerciseImageProvider", "🎯 Using preloaded image (sync) for '$exerciseName'")
            return ExerciseVisual.ImageUrl(preloadedUrl)
        }

        // 2. Verificar cache en memoria
        memoryCache[cacheKey]?.let { cachedUrl ->
            Log.d("ExerciseImageProvider", "🗂️ Using cached image (sync) for '$exerciseName'")
            return ExerciseVisual.ImageUrl(cachedUrl)
        }

        // 3. Fallback inmediato a icono
        val icon = ExerciseIconProvider.getBestIcon(exerciseName, muscleGroups)
        Log.d("ExerciseImageProvider", "⚡ Using icon (sync) for '$exerciseName'")
        return ExerciseVisual.Icon(icon)
    }
}
