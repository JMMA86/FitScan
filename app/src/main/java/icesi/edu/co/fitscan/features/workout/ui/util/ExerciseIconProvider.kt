package icesi.edu.co.fitscan.features.workout.ui.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

object ExerciseIconProvider {
    
    /**
     * Mapeo de ejercicios a iconos específicos - EXPANDIDO
     */
    private val exerciseIcons = mapOf(
        // === EJERCICIOS DE PECHO ===
        "flexiones" to Icons.Default.SportsGymnastics,
        "push ups" to Icons.Default.SportsGymnastics,
        "press de banca" to Icons.Default.FitnessCenter,
        "bench press" to Icons.Default.FitnessCenter,
        "press inclinado" to Icons.Default.FitnessCenter,
        "incline press" to Icons.Default.FitnessCenter,
        "aperturas" to Icons.Default.OpenInFull,
        "chest fly" to Icons.Default.OpenInFull,
        "fondos" to Icons.Default.SportsGymnastics,
        "dips" to Icons.Default.SportsGymnastics,
        "cruces" to Icons.Default.OpenInFull,
        "cable crossover" to Icons.Default.OpenInFull,
        
        // === EJERCICIOS DE ESPALDA ===
        "dominadas" to Icons.Default.SportsGymnastics,
        "pull ups" to Icons.Default.SportsGymnastics,
        "pullups" to Icons.Default.SportsGymnastics,
        "chin ups" to Icons.Default.SportsGymnastics,
        "jalones" to Icons.Default.Rowing,
        "lat pulldown" to Icons.Default.Rowing,
        "remo" to Icons.Default.Rowing,
        "rowing" to Icons.Default.Rowing,
        "remo con barra" to Icons.Default.FitnessCenter,
        "barbell row" to Icons.Default.FitnessCenter,
        "remo con mancuerna" to Icons.Default.FitnessCenter,
        "dumbbell row" to Icons.Default.FitnessCenter,
        "encogimientos" to Icons.Default.FitnessCenter,
        "shrugs" to Icons.Default.FitnessCenter,
        
        // === EJERCICIOS DE PIERNAS ===
        "sentadillas" to Icons.Default.DirectionsRun,
        "squats" to Icons.Default.DirectionsRun,
        "peso muerto" to Icons.Default.FitnessCenter,
        "deadlift" to Icons.Default.FitnessCenter,
        "prensa de piernas" to Icons.Default.FitnessCenter,
        "leg press" to Icons.Default.FitnessCenter,
        "estocadas" to Icons.Default.DirectionsWalk,
        "lunges" to Icons.Default.DirectionsWalk,
        "zancadas" to Icons.Default.DirectionsWalk,
        "walking lunges" to Icons.Default.DirectionsWalk,
        "extensiones de cuadriceps" to Icons.Default.DirectionsRun,
        "leg extension" to Icons.Default.DirectionsRun,
        "curl de femoral" to Icons.Default.DirectionsRun,
        "leg curl" to Icons.Default.DirectionsRun,
        "pantorrillas" to Icons.Default.DirectionsWalk,
        "calf raise" to Icons.Default.DirectionsWalk,
        "hip thrust" to Icons.Default.FitnessCenter,
        
        // === EJERCICIOS DE HOMBROS ===
        "press de hombros" to Icons.Default.FitnessCenter,
        "shoulder press" to Icons.Default.FitnessCenter,
        "press militar" to Icons.Default.FitnessCenter,
        "military press" to Icons.Default.FitnessCenter,
        "elevaciones laterales" to Icons.Default.OpenInFull,
        "lateral raise" to Icons.Default.OpenInFull,
        "elevaciones frontales" to Icons.Default.ExpandLess,
        "front raise" to Icons.Default.ExpandLess,
        "elevaciones posteriores" to Icons.Default.OpenInFull,
        "rear delt fly" to Icons.Default.OpenInFull,
        "vuelos posteriores" to Icons.Default.OpenInFull,
        "reverse fly" to Icons.Default.OpenInFull,
        
        // === EJERCICIOS DE BRAZOS ===
        "curl de biceps" to Icons.Default.FitnessCenter,
        "bicep curl" to Icons.Default.FitnessCenter,
        "martillo" to Icons.Default.FitnessCenter,
        "hammer curl" to Icons.Default.FitnessCenter,
        "curl concentrado" to Icons.Default.FitnessCenter,
        "concentration curl" to Icons.Default.FitnessCenter,
        "extensiones de triceps" to Icons.Default.FitnessCenter,
        "tricep extension" to Icons.Default.FitnessCenter,
        "extensiones por encima" to Icons.Default.ExpandLess,
        "overhead tricep extension" to Icons.Default.ExpandLess,
        "patadas de triceps" to Icons.Default.FitnessCenter,
        "tricep kickback" to Icons.Default.FitnessCenter,
        
        // === EJERCICIOS DE CORE ===
        "abdominales" to Icons.Default.SportsGymnastics,
        "crunches" to Icons.Default.SportsGymnastics,
        "abs" to Icons.Default.SportsGymnastics,
        "plancha" to Icons.Default.SportsGymnastics,
        "plank" to Icons.Default.SportsGymnastics,
        "elevaciones de piernas" to Icons.Default.ExpandLess,
        "leg raise" to Icons.Default.ExpandLess,
        "russian twist" to Icons.Default.RotateLeft,
        "bicycle crunches" to Icons.Default.SportsGymnastics,
        "mountain climbers" to Icons.Default.DirectionsRun,
        
        // === EJERCICIOS CARDIOVASCULARES ===
        "correr" to Icons.Default.DirectionsRun,
        "running" to Icons.Default.DirectionsRun,
        "trotar" to Icons.Default.DirectionsRun,
        "jogging" to Icons.Default.DirectionsRun,
        "caminar" to Icons.Default.DirectionsWalk,
        "walking" to Icons.Default.DirectionsWalk,
        "bicicleta" to Icons.Default.DirectionsBike,
        "cycling" to Icons.Default.DirectionsBike,
        "bike" to Icons.Default.DirectionsBike,
        "saltar cuerda" to Icons.Default.SportsGymnastics,
        "jump rope" to Icons.Default.SportsGymnastics,
        "eliptica" to Icons.Default.DirectionsRun,
        "elliptical" to Icons.Default.DirectionsRun,
        
        // === EJERCICIOS FUNCIONALES ===
        "burpees" to Icons.Default.DirectionsRun,
        "jumping jacks" to Icons.Default.DirectionsRun,
        "thrusters" to Icons.Default.FitnessCenter,
        "wall balls" to Icons.Default.SportsBasketball,
        "wall ball" to Icons.Default.SportsBasketball,
        "box jumps" to Icons.Default.ExpandLess,
        "box jump" to Icons.Default.ExpandLess,
        "kettlebell swing" to Icons.Default.FitnessCenter,
        "battle ropes" to Icons.Default.SportsGymnastics,
        "turkish get up" to Icons.Default.SportsGymnastics,
        
        // === EJERCICIOS OLÍMPICOS ===
        "clean" to Icons.Default.FitnessCenter,
        "snatch" to Icons.Default.FitnessCenter,
        "jerk" to Icons.Default.FitnessCenter,
        "clean and jerk" to Icons.Default.FitnessCenter,
        "clean and press" to Icons.Default.FitnessCenter,
        
        // === EJERCICIOS DE ESTIRAMIENTO/FLEXIBILIDAD ===
        "yoga" to Icons.Default.SelfImprovement,
        "estiramiento" to Icons.Default.SelfImprovement,
        "stretching" to Icons.Default.SelfImprovement,
        "foam roller" to Icons.Default.SelfImprovement,
        
        // === EJERCICIOS DE MÁQUINAS ===
        "cruce de cables" to Icons.Default.OpenInFull,
        "cable crossover" to Icons.Default.OpenInFull,
        "remo en polea" to Icons.Default.Rowing,
        "cable row" to Icons.Default.Rowing,
        "face pulls" to Icons.Default.Rowing,
        
        // === EJERCICIOS ESPECÍFICOS ===
        "upright row" to Icons.Default.ExpandLess,
        "close grip bench" to Icons.Default.FitnessCenter,
        "overhead press" to Icons.Default.ExpandLess,
        "lateral pulldown" to Icons.Default.Rowing,
        "chest press" to Icons.Default.FitnessCenter,
        "shoulder shrugs" to Icons.Default.FitnessCenter,
        
        // === EJERCICIOS DE EQUILIBRIO/ESTABILIDAD ===
        "bosu ball" to Icons.Default.SportsGymnastics,
        "stability ball" to Icons.Default.SportsGymnastics,
        "balance board" to Icons.Default.SportsGymnastics,
        "single leg" to Icons.Default.DirectionsRun,
        
        // === EJERCICIOS DE RESISTENCIA ===
        "banda de resistencia" to Icons.Default.FitnessCenter,
        "resistance band" to Icons.Default.FitnessCenter,
        "suspension trainer" to Icons.Default.SportsGymnastics,
        "trx" to Icons.Default.SportsGymnastics,
        
        // === EJERCICIOS DE ALTA INTENSIDAD ===
        "sledgehammer" to Icons.Default.FitnessCenter,
        "tire flip" to Icons.Default.FitnessCenter,
        "farmers walk" to Icons.Default.DirectionsWalk,
        "sled push" to Icons.Default.DirectionsRun,
        "rope climb" to Icons.Default.SportsGymnastics
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
