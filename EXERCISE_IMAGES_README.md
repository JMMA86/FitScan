# Sistema de Imágenes Dinámicas para Ejercicios

## Resumen
Se ha implementado un sistema completo para mostrar imágenes de ejercicios sin necesidad de almacenarlas en la base de datos o el servidor.

## Componentes Creados

### 1. ExerciseImageProvider
**Ubicación:** `features/workout/ui/util/ExerciseImageProvider.kt`

Utilidades para generar URLs de imágenes dinámicas:
- `getExerciseImageUrl()` - URL básica usando Unsplash
- `getTranslatedExerciseImageUrl()` - Traduce ejercicios al inglés para mejores resultados
- `getImageByMuscleGroup()` - Imagen basada en grupo muscular
- `getDefaultExerciseImageUrl()` - Imagen por defecto

### 2. ExerciseImageHeader
**Ubicación:** `features/workout/ui/components/ExerciseImageHeader.kt`

Componente para headers grandes (180dp) con:
- Carga asíncrona de imágenes usando Coil
- Fallback a imagen por grupo muscular
- Gradiente como fallback final
- Indicador de carga

### 3. ExerciseImageThumbnail
**Ubicación:** `features/workout/ui/components/ExerciseImageThumbnail.kt`

Componente para miniaturas (60dp por defecto):
- Versión circular o rectangular
- Ideal para listas y cards
- Fallbacks automáticos

### 4. ExerciseIconProvider
**Ubicación:** `features/workout/ui/util/ExerciseIconProvider.kt`

Sistema de iconos como alternativa:
- Mapeo de ejercicios a iconos Material
- Iconos por grupo muscular
- Función inteligente `getBestIcon()`

### 5. ExerciseIconHeader
**Ubicación:** `features/workout/ui/components/ExerciseIconHeader.kt`

Header usando solo iconos vectoriales:
- Sin dependencia de internet
- Siempre funciona
- Diseño consistente

## Uso

### En ExerciseDetailScreen
```kotlin
ExerciseImageHeader(
    exerciseName = "Flexiones",
    muscleGroups = "pecho,triceps",
    height = 180
)
```

### En listas de ejercicios
```kotlin
ExerciseImageThumbnail(
    exerciseName = "Sentadillas",
    muscleGroups = "piernas",
    size = 60,
    isCircular = true
)
```

### Solo iconos
```kotlin
ExerciseIconHeader(
    exerciseName = "Dominadas",
    muscleGroups = "espalda",
    height = 180
)
```

## APIs Utilizadas

### Unsplash Source API
- **URL:** `https://source.unsplash.com/{width}x{height}/?{query}`
- **Ventajas:** Gratuito, no requiere API key, imágenes de alta calidad
- **Limitaciones:** No control sobre imágenes específicas

### Alternativas Disponibles
1. **Pexels API** - Requiere API key pero más control
2. **Lorem Picsum** - Imágenes aleatorias, menos específicas
3. **Iconos Material** - Siempre disponibles, no requieren internet

## Configuración

### Dependencias Requeridas
```kotlin
implementation("io.coil-kt:coil-compose:2.7.0") // Ya incluido
```

### Permisos Requeridos
```xml
<uses-permission android:name="android.permission.INTERNET"/> <!-- Ya incluido -->
```

## Ventajas del Sistema

1. **Sin almacenamiento:** No necesitas guardar imágenes
2. **Dinámico:** Se adapta automáticamente a nuevos ejercicios
3. **Fallbacks robustos:** Siempre muestra algo coherente
4. **Escalable:** Funciona con cualquier número de ejercicios
5. **Multilingual:** Traduce términos para mejores resultados
6. **Offline-friendly:** Opción de iconos que no requiere internet

## Personalización

Para agregar más ejercicios o mejorar las traducciones, edita los mapas en:
- `ExerciseImageProvider.exerciseTranslations`
- `ExerciseIconProvider.exerciseIcons`
- `ExerciseIconProvider.muscleGroupIcons`
