# FitScan

## Integrantes

- Cristian Botina (A00395008)  
- Óscar Gómez (A00394142)  
- Juan Marín (A00382037)  
- Felipe Rojas (A00393918)  
- Danna López (A00395625)

## ¿Qué es FitScan?

**FitScan** es una aplicación móvil creada para ayudar a personas que quieren romper su rutina sedentaria, aprender a entrenar correctamente y mejorar su salud de manera accesible y sencilla.  
La aplicación ofrece planes de entrenamiento personalizados, seguimiento de métricas de salud como peso e IMC, y la posibilidad de registrar el progreso semanal para mantener la motivación alta.

Nuestros objetivos principales son:

- Ofrecer entrenamientos adaptados al nivel de cada usuario, especialmente principiantes.
- Brindar una experiencia de uso sencilla y amigable para personas sin experiencia previa en fitness.
- Permitir el seguimiento fácil de métricas de progreso.
- Motivar a los usuarios a mantener la constancia mediante resultados visibles y rutas claras hacia sus metas deportivas o de salud.
- Fomentar el acceso a una herramienta económica y de calidad, adecuada para quienes buscan mejorar su bienestar físico sin complicaciones.

FitScan está pensado para ser el primer paso seguro y accesible para cualquier persona que desee transformar su estilo de vida.

## Demo de autenticación

Puedes ver una demostración del flujo del sprint 1 de autenticación de usuarios en el siguiente enlace:  
👉 **[Video de autenticación](https://youtube.com/shorts/ox61H1Qyyjo?feature=share)**

Puedes ver las funcionalidades de workout y estadisticas implementadas para el sprint 2 en el siguiente enlace:
 👉 **[Video de funcionalidades](https://youtu.be/iRs79wpJSSQ)**

## Caracteristicas añadidas este sprint

Aquí un resumen de lo que se ha implementado en las pantallas de workout, statistics y home:

- 🏋️‍♂️ **Workout:**
  - Pantalla para realizar rutinas (`PerformWorkoutScreen`): muestra ejercicio actual, siguiente, lista de ejercicios restantes, control de sets, temporizador y navegación entre ejercicios.
  - Pantalla de detalle de rutina (`WorkoutDetailScreen`): muestra información de la rutina y permite iniciar el entrenamiento.
  - Pantalla para crear rutinas (`CreateWorkoutScreen`): permite crear nuevas rutinas personalizadas.
  - Pantalla de detalle de ejercicio (`ExerciseDetailScreen`): muestra información detallada de cada ejercicio.

- 📊 **Statistics:**
  - Pantalla de estadísticas generales (`StatisticsScreen`): visualización de progreso, ejercicios completados y métricas relevantes.
  - Pantalla de progreso por ejercicio (`ExerciseProgressScreen`): muestra el avance específico en cada ejercicio.
  - Pantalla de progreso visual (`ProgressPhotoScreen`): permite ver y registrar fotos de progreso físico.

- 🏠 **Home (Dashboard):**
  - Pantalla principal (`DashboardScreen`): acceso rápido a rutinas, estadísticas, perfil y otras funcionalidades.
  - Resumen del estado actual del usuario y accesos directos a las acciones principales.

¡Estas funcionalidades mejoran la experiencia del usuario y facilitan el seguimiento y gestión de su entrenamiento! 🚀

## Roles y Permisos

La aplicación maneja **dos roles principales**:

| Rol               | Permisos principales                                                                                                                                                                |
|-------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Usuario**        | - Registrarse y autenticarse en la aplicación<br>- Completar y editar su perfil personal (edad, altura, peso, medidas, nivel de entrenamiento, alergias)<br>- Crear y editar sus propios entrenamientos<br>- Buscar ejercicios y agregar sugerencias de IA a sus entrenamientos<br>- Consultar y seguir rutinas populares<br>- Crear y consultar su plan de alimentación personalizado<br>- Editar y consultar sus planes de alimentación<br>- Registrar y consultar métricas de salud (peso, calorías, distancia, horas de ejercicio, etc.)<br>- Subir fotos de progreso mensuales<br>- Visualizar estadísticas personales y gráficas de evolución |
| **Administrador** | - Consultar la lista completa de usuarios registrados<br>- Ver y editar la información básica de los usuarios (perfil, métricas, medidas, etc.)<br>- Consultar las rutinas públicas creadas por los usuarios<br>- Consultar las rutinas privadas de cada usuario<br>- Consultar los planes de alimentación asociados a cada usuario<br>- Consultar las estadísticas y registros de progreso de cualquier usuario |

## Tecnologías utilizadas

- **Android Studio** con **Jetpack Compose** para el desarrollo de la aplicación móvil.
- **Directus** (gestionado con **Docker**) como backend para:
  - Autenticación de usuarios.
  - Gestión y almacenamiento de datos relacionados con usuarios, entrenamientos, métricas y progresos.

## Configuración del Entorno

### 🔑 Variables de Entorno

FitScan utiliza APIs externas que requieren configuración de keys:

1. **Copia el archivo de configuración:**

   ```bash
   cp .env.example .env
   ```

2. **Configura tu API Key de Unsplash:**
   - Ve a [Unsplash Developers](https://unsplash.com/developers)
   - Crea una cuenta gratuita
   - Crea una nueva aplicación
   - Copia tu "Access Key"
   - Pégala en `.env` reemplazando `tu_access_key_aqui`

3. **El archivo `.env` debería verse así:**

   ```env
   UNSPLASH_ACCESS_KEY=tu_access_key_real_aqui
   UNSPLASH_BASE_URL=https://api.unsplash.com/
   ```

⚠️ **Importante:** Nunca subas el archivo `.env` a git. Ya está incluido en `.gitignore`.
