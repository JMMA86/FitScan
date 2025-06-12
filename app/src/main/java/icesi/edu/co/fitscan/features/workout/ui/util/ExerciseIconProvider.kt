package icesi.edu.co.fitscan.features.workout.ui.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

object ExerciseIconProvider {
    
    /**
     * Mapeo de ejercicios a iconos específicos
     */
    private val exerciseIcons = mapOf(
        // Ejercicios de empuje
        "flexiones" to Icons.Default.SportsGymnastics,
        "press de banca" to Icons.Default.FitnessCenter,
        "press de hombros" to Icons.Default.FitnessCenter,
        
        // Ejercicios de tirón
        "dominadas" to Icons.Default.SportsGymnastics,
        "remo" to Icons.Default.Rowing,
        "curl de biceps" to Icons.Default.FitnessCenter,
        
        // Ejercicios de piernas
        "sentadillas" to Icons.Default.DirectionsRun,
        "peso muerto" to Icons.Default.FitnessCenter,
        "prensa de piernas" to Icons.Default.FitnessCenter,
        "estocadas" to Icons.Default.DirectionsWalk,
        
        // Ejercicios de core
        "abdominales" to Icons.Default.FitnessCenter,
        "plancha" to Icons.Default.SportsGymnastics,
        "crunches" to Icons.Default.FitnessCenter,
        
        // Ejercicios cardiovasculares
        "burpees" to Icons.Default.DirectionsRun,
        "jumping jacks" to Icons.Default.DirectionsRun,
        "correr" to Icons.Default.DirectionsRun,
        "caminar" to Icons.Default.DirectionsWalk,
        
        // Ejercicios de funcionales
        "kettlebell swing" to Icons.Default.FitnessCenter,
        "battle ropes" to Icons.Default.SportsGymnastics
    )
    
    /**
     * Mapeo de grupos musculares a iconos
     */
    private val muscleGroupIcons = mapOf(
        "pecho" to Icons.Default.FitnessCenter,
        "espalda" to Icons.Default.FitnessCenter,
        "piernas" to Icons.Default.DirectionsRun,
        "brazos" to Icons.Default.FitnessCenter,
        "hombros" to Icons.Default.FitnessCenter,
        "abdomen" to Icons.Default.SportsGymnastics,
        "biceps" to Icons.Default.FitnessCenter,
        "triceps" to Icons.Default.FitnessCenter,
        "cuadriceps" to Icons.Default.DirectionsRun,
        "gluteos" to Icons.Default.DirectionsRun,
        "pantorrillas" to Icons.Default.DirectionsWalk,
        "cardio" to Icons.Default.DirectionsRun,
        "funcional" to Icons.Default.SportsGymnastics
    )
    
    /**
     * Obtiene el icono para un ejercicio específico
     */
    fun getExerciseIcon(exerciseName: String): ImageVector {
        val cleanName = exerciseName.lowercase().trim()
        return exerciseIcons[cleanName] ?: Icons.Default.FitnessCenter
    }
    
    /**
     * Obtiene el icono basado en el grupo muscular
     */
    fun getIconByMuscleGroup(muscleGroups: String?): ImageVector {
        val primaryMuscle = muscleGroups?.split(",")?.firstOrNull()?.trim()?.lowercase()
        return muscleGroupIcons[primaryMuscle] ?: Icons.Default.FitnessCenter
    }
    
    /**
     * Obtiene el mejor icono combinando nombre y grupo muscular
     */
    fun getBestIcon(exerciseName: String, muscleGroups: String?): ImageVector {
        val cleanName = exerciseName.lowercase().trim()
        
        // Intentar primero con el nombre exacto
        exerciseIcons[cleanName]?.let { return it }
        
        // Si no existe, buscar por palabras clave en el nombre
        exerciseIcons.keys.forEach { key ->
            if (cleanName.contains(key) || key.contains(cleanName)) {
                return exerciseIcons[key]!!
            }
        }
        
        // Fallback al grupo muscular
        return getIconByMuscleGroup(muscleGroups)
    }
}
