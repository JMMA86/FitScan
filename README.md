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

Puedes ver una demostración del flujo de autenticación de usuarios en el siguiente enlace:  
👉 **[Video de autenticación](LINK_DEL_VIDEO)** *(pendiente por actualizar)*

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
