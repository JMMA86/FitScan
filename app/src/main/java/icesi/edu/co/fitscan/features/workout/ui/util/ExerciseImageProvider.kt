package icesi.edu.co.fitscan.features.workout.ui.util

object ExerciseImageProvider {
    
    /**
     * Genera una URL de imagen usando Unsplash basada en el nombre del ejercicio
     */
    fun getExerciseImageUrl(exerciseName: String, width: Int = 400, height: Int = 300): String {
        val searchQuery = cleanExerciseName(exerciseName)
        return "https://source.unsplash.com/${width}x${height}/?$searchQuery,fitness,exercise,gym"
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
        "patadas de triceps" to "tricep+kickback",
        
        // Ejercicios cardiovasculares
        "correr" to "running",
        "trotar" to "jogging",
        "caminar" to "walking",
        "bicicleta" to "cycling",
        "saltar cuerda" to "jump+rope",
        "eliptica" to "elliptical",
        
        // Ejercicios funcionales
        "thrusters" to "thrusters",
        "wall balls" to "wall+balls",
        "box jumps" to "box+jumps",
        "mountain climbers" to "mountain+climbers"
    )
    
    /**
     * Versión mejorada que traduce ejercicios comunes al inglés
     */
    fun getTranslatedExerciseImageUrl(exerciseName: String, width: Int = 400, height: Int = 300): String {
        val cleanName = cleanExerciseName(exerciseName)
        val translatedName = exerciseTranslations[cleanName] ?: cleanName
        return "https://source.unsplash.com/${width}x${height}/?$translatedName,fitness,exercise,gym"
    }
    
    /**
     * URL de imagen por defecto cuando no se encuentra una específica
     */
    fun getDefaultExerciseImageUrl(width: Int = 400, height: Int = 300): String {
        return "https://source.unsplash.com/${width}x${height}/?fitness,gym,exercise,workout"
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
            
            // Grupos específicos
            "biceps" to "bicep+workout",
            "triceps" to "tricep+workout",
            "cuadriceps" to "quadriceps+workout",
            "gluteos" to "glutes+workout",
            "pantorrillas" to "calves+workout",
            "femorales" to "hamstring+workout",
            "isquiotibiales" to "hamstring+workout",
            
            // Sinónimos y variaciones
            "core" to "core+workout",
            "abs" to "abs+workout",
            "deltoides" to "shoulder+workout",
            "dorsal" to "back+workout",
            "pectorales" to "chest+workout",
            "gemelos" to "calves+workout",
            "muslos" to "leg+workout",
            
            // Grupos compuestos
            "tren superior" to "upper+body+workout",
            "tren inferior" to "lower+body+workout",
            "cardio" to "cardio+exercise",
            "funcional" to "functional+training"
        )
        
        val searchTerm = muscleImageMap[primaryMuscle] ?: "fitness+workout"
        return "https://source.unsplash.com/400x300/?$searchTerm,gym,exercise"
    }
    
    /**
     * Búsqueda inteligente que combina nombre y grupo muscular
     */
    fun getSmartExerciseImageUrl(exerciseName: String, muscleGroups: String?, width: Int = 400, height: Int = 300): String {
        val cleanName = cleanExerciseName(exerciseName)
        
        // Intentar traducción exacta primero
        exerciseTranslations[cleanName]?.let { translatedName ->
            return "https://source.unsplash.com/${width}x${height}/?$translatedName,fitness,exercise,gym"
        }
        
        // Buscar por palabras clave en el nombre
        val nameKeywords = cleanName.split("+").filter { it.length > 2 }
        val foundTranslation = exerciseTranslations.entries.find { (key, _) ->
            nameKeywords.any { keyword -> key.contains(keyword) || keyword.contains(key) }
        }?.value
        
        if (foundTranslation != null) {
            return "https://source.unsplash.com/${width}x${height}/?$foundTranslation,fitness,exercise,gym"
        }
        
        // Si no encuentra traducción, combinar con grupo muscular
        val primaryMuscle = muscleGroups?.split(",")?.firstOrNull()?.trim()?.lowercase()
        val muscleSearchTerm = primaryMuscle?.let { muscle ->
            mapOf(
                "pecho" to "chest",
                "espalda" to "back", 
                "piernas" to "legs",
                "brazos" to "arms",
                "hombros" to "shoulders",
                "abdomen" to "core"
            )[muscle] ?: muscle
        }
        
        val combinedSearch = if (muscleSearchTerm != null) {
            "$cleanName+$muscleSearchTerm+exercise"
        } else {
            "$cleanName+fitness+exercise"
        }
        
        return "https://source.unsplash.com/${width}x${height}/?$combinedSearch,gym"
    }
}
