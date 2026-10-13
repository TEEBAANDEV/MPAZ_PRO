# MPAZ PRO 2.º - Aplicación Móvil de Apoyo Didáctico para Segundo Básico

**Asignatura:** Desarrollo de Aplicaciones Móviles (DSY1105)  
**Institución:** Duoc UC - Escuela de Informática y Telecomunicaciones  
**Caso de Estudio:** Escuela Pública Escritora Marcela Paz (San Bernardo)  
**Evaluación:** Evaluación Parcial 2 (Encargo Grupal - 8%)  

---

## 📖 1. Descripción del Proyecto

**MPAZ PRO 2.º** es una aplicación móvil nativa Android orientada a optimizar el proceso de enseñanza y aprendizaje en estudiantes de **Segundo Básico**, digitalizando el trabajo pedagógico tradicional basado en guías impresas. 

La solución centraliza contenidos didácticos, actividades interactivas progresivas y pruebas finales por Objetivo de Aprendizaje (OA) del Mineduc, integrando un flujo pedagógico completo:
1. **Jefatura UTP:** Administra asignaturas, revisa y aprueba Objetivos de Aprendizaje (OA) y valida fuentes curriculares.
2. **Docente de 2° Básico:** Selecciona unidades autorizadas, asigna al curso fijando límites de intentos y fechas, monitorea avances en tiempo real y registra observaciones pedagógicas sobre informes de rendimiento.
3. **Estudiante de 2° Básico:** Interfaz especialmente adaptada a lectores iniciales (alta legibilidad, oraciones concisas, baja carga cognitiva) con soporte multimedia sonoro mediante lectura en voz alta (Text-To-Speech) y retroalimentación táctil inmediata (vibración).

---

## 👥 2. Integrantes del Equipo

Damian Caceres - 20.712.243-1 / Seccion 007D

---

## 🏗️ 3. Arquitectura y Stack Técnico

El proyecto ha sido estructurado siguiendo estrictamente las mejores prácticas modernas de desarrollo en Android y los principios del indicador **IE 2.3.1**:

* **Lenguaje:** Kotlin 2.2.10
* **Entorno de Desarrollo:** Android Studio
* **Interfaz de Usuario:** Jetpack Compose + Material Design 3
* **Arquitectura:** **MVVM (Model - View - ViewModel)** organizada según la guía oficial de clase (*Arquitectura de una APP* - Docente Carlos Abarzúa Castro):
  * `app/`: Punto de entrada (`MainActivity.kt`, `AplicacionMpaz.kt`).
  * `data/`:
    * `data/model/`: Modelos de datos y estados de formulario (`ModelosFormulario.kt` con `FormularioLoginModel`, `FormularioObjetivoModel`, `FormularioObservacionModel` y sus clases de error siguiendo `MiAppFormulario`).
    * `data/repository/`: `RepositorioApp` centralizando las consultas a Room.
    * `data/local/`: Base de datos SQLite Room (`BaseDatosApp`, `AppDao` unificado, `Entidades.kt` e `InicializadorBaseDatos`).
  * `viewmodel/`: ViewModels reactivos (`AutenticacionViewModel` y `MpazViewModel`) con `StateFlow`, conectando Room, recursos nativos y formularios.
  * `ui/`:
    * `ui/components`: Elementos reutilizables basados en los proyectos de clase:
      * `AppDrawer.kt`: Menú lateral deslizante (`ModalNavigationDrawer` + `ModalDrawerSheet` + `NavigationDrawerItem`) basado en *MenuDeslizanteCompose*.
      * `AppTopBar.kt`: Barra superior con control de menú hamburguesa y opciones.
      * `NuevoMenu.kt`: Menú desplegable contextual en barra superior basado en *AppMenu2.0* y *MiApp2.0*.
      * `AppBottomBar.kt`: Barra de navegación inferior (`NavigationBar` + `NavigationBarItem`) basado en *MiApp2.0*.
      * `InsigniaEstado.kt`, `BarraSuperiorMpaz.kt`.
    * `ui/screens`: Pantallas directas de cada perfil (`PantallaInicioSesion.kt`, `PantallaUtp.kt`, `PantallaDocente.kt`, `PantallaEstudiante.kt`).
    * `ui/theme`: Colores, tipografías y tema visual Material 3 (*Theme.kt*, *Color.kt*, *Type.kt*).
    * `ui/navigation`: Navegación desacoplada con Navigation Compose (`GrafoNavegacion`, `DestinoNavegacion`).
  * `native/`: Acceso a recursos de hardware nativos (`AyudanteTextoAVoz`, `AyudanteVibracion`, `AyudanteCompartirInforme`).

---

## 📱 4. Recursos Nativos del Dispositivo (IE 2.4.1)

Para cumplir y superar el indicador de logro que exige el acceso a al menos **dos recursos nativos del dispositivo** integrados con coherencia en la interfaz:

1. **Text-To-Speech Nativo de Android (`AyudanteTextoAVoz` / `android.speech.tts.TextToSpeech`):**
   * *Justificación pedagógica:* En segundo básico los alumnos son lectores iniciales en proceso de autonomía. La app proporciona un botón parlante interactivo (`BotonAltavozAudio`) que lee con voz clara en español chileno/latino las explicaciones didácticas, enunciados de actividades y preguntas de evaluación sin requerir grabaciones externas ni streaming pesado.
2. **Retroalimentación Háptica / Vibración Física (`AyudanteVibracion` / `android.os.Vibrator` & `VibrationEffect`):**
   * *Justificación pedagógica:* Proporciona feedback táctil inmediato en los desafíos: vibración suave al acertar una respuesta, vibración de doble alerta al equivocarse para motivar al reintento, y patrón rítmico de celebración al finalizar la evaluación.
3. **Compartición Nativa de Reportes (`AyudanteCompartirInforme` / `Intent.ACTION_SEND`):**
   * *Justificación pedagógica:* Permite al docente descargar y exportar los informes pedagógicos individuales consolidados hacia correo electrónico, WhatsApp o aplicaciones de notas del sistema.

---

## 🛡️ 5. Formularios y Validaciones Desacopladas (IE 2.1.2 & IE 2.2.1)

Todos los formularios disponen de retroalimentación visual inmediata ante errores (bordes rojos, íconos de alerta `Icons.Default.Error`, textos de ayuda animados con `AnimatedVisibility`), con su lógica 100% desacoplada en `FormValidators`:

1. **Formulario de Autenticación / Login:**
   * Validación de usuario (no vacío, mínimo 3 caracteres, caracteres alfanuméricos válidos).
   * Validación de contraseña (mínimo 4 caracteres).
2. **Formulario UTP de Gestión Curricular:**
   * Código de objetivo (validación de formato oficial Mineduc: `OA 04`, `OA 01`, etc.).
   * Título del objetivo (mínimo 5 caracteres).
   * Descripción pedagógica (mínimo 10 caracteres).
   * Selección de estado curricular (`BORRADOR`, `EN_REVISION`, `AUTORIZADO`, `PUBLICADO`, `DESHABILITADO`).
3. **Formulario Docente de Asignación:**
   * Límite de intentos para la prueba final (validación numérica estricta entre 1 y 10 intentos).
   * Rango de fechas de disponibilidad (validación estricta de formato `AAAA-MM-DD`).
4. **Formulario Docente de Observaciones Pedagógicas:**
   * Validación de observación cualitativa obligatoria antes de validar el informe.

---

## ✨ 6. Animaciones Visuales Funcionales (IE 2.2.2)

* **AnimatedVisibility:** Expansión y desvanecimiento suave de mensajes de error de validación, confirmaciones de éxito y retroalimentaciones de actividades.
* **animateContentSize:** Redimensionamiento fluido en tarjetas de objetivos y opciones de selección.
* **Pulso infinito en Audio:** El botón de Text-To-Speech emite una animación de pulsación rítmica continua (`rememberInfiniteTransition`) mientras el motor de voz está activo.
* **Barras de Progreso Animadas:** Animación continua de avance en lecciones, actividades y pruebas finales.

---

## 🔑 7. Credenciales de Acceso y Ramas del Repositorio

Para ingresar en la versión formal (`main`), ingrese las credenciales registradas según el perfil que desee evaluar:

| Perfil | Usuario / RUT | Contraseña | Rol en la Aplicación |
| :--- | :--- | :--- | :--- |
| **Jefatura UTP** | `utp` | `1234` | Administra y aprueba las 4 asignaturas y objetivos curriculares. |
| **Docente 2° Básico** | `docente` | `1234` | Asigna al curso, monitorea a los estudiantes y registra observaciones. |
| **Estudiante** | `lucas` | `1234` | Lee contenidos con audio TTS y resuelve desafíos con vibración háptica. |

> [!NOTE]
> **Ramas disponibles en el repositorio:**
> * **`main` (Entrega Formal):** Interfaz limpia y formal con login estándar (ingreso por usuario y clave).
> * **`pruebas` (Demostración Rápida):** Incluye botones de acceso rápido directo ("UTP", "Docente", "Estudiante") en la pantalla de bienvenida para facilitar pruebas ágiles de evaluación presencial.

---

## 🚀 8. Pasos para Compilar y Ejecutar

1. **Clonar o abrir el repositorio** en **Android Studio** (versión Ladybug o superior recomendada).
2. Esperar a que **Gradle Sync** finalice exitosamente.
3. Conectar un dispositivo físico Android o iniciar un emulador con API 24 o superior (recomendado API 34 o 35).
4. Hacer clic en **Run 'app'** (`Shift + F10`) o ejecutar en terminal:
   ```bash
   ./gradlew installDebug
   ```
5. En la pantalla inicial, utilice las credenciales o los botones de acceso directo para explorar los flujos de **UTP**, **Docente** y **Estudiante**.

---

## 📋 9. Planificación en Trello y Control de Versiones

* **Tablero Trello del Proyecto:** [Enlace a su tablero de Trello]
  * Columnas implementadas: *Backlog*, *En Progreso*, *Pruebas de Validación*, *Completado*.
  * Distribución equitativa de tarjetas entre ambos integrantes de la pareja.
* **Flujo Git:**
  * Commits atómicos y descriptivos siguiendo la convención técnica: `feat:`, `fix:`, `refactor:`, `docs:`.
