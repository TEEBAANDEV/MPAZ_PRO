package com.example.mpaz_pro.data.local

import com.example.mpaz_pro.data.local.entity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object InicializadorBaseDatos {

    suspend fun poblarDatosIniciales(baseDatos: BaseDatosApp) {
        withContext(Dispatchers.IO) {
            val usuarioDao = baseDatos.usuarioDao()
            val asignaturaDao = baseDatos.asignaturaDao()
            val objetivoDao = baseDatos.objetivoAprendizajeDao()
            val contenidoDao = baseDatos.contenidoDidacticoDao()
            val desafioDao = baseDatos.desafioDao()
            val preguntaEvaluacionDao = baseDatos.preguntaEvaluacionDao()
            val progresoDao = baseDatos.progresoEstudianteDao()
            val informeDao = baseDatos.informePedagogicoDao()

            // 1. Usuarios ficticios de demostración pedagógica
            val usuarios = listOf(
                UsuarioEntity(
                    id = "usr_utp",
                    nombreUsuario = "utp",
                    claveHash = "1234",
                    nombreCompleto = "Prof. Claudia Reyes (Jefatura UTP)",
                    rol = RolUsuario.UTP,
                    grado = "Primer Ciclo",
                    curso = "Colegio Marcela Paz"
                ),
                UsuarioEntity(
                    id = "usr_docente",
                    nombreUsuario = "docente",
                    claveHash = "1234",
                    nombreCompleto = "Prof. Mario Silva",
                    rol = RolUsuario.DOCENTE,
                    grado = "2° Básico",
                    curso = "2° Básico A"
                ),
                UsuarioEntity(
                    id = "usr_estudiante_1",
                    nombreUsuario = "lucas",
                    claveHash = "1234",
                    nombreCompleto = "Lucas Martínez",
                    rol = RolUsuario.ESTUDIANTE,
                    grado = "2° Básico",
                    curso = "2° Básico A"
                ),
                UsuarioEntity(
                    id = "usr_estudiante_2",
                    nombreUsuario = "sofia",
                    claveHash = "1234",
                    nombreCompleto = "Sofía Morales",
                    rol = RolUsuario.ESTUDIANTE,
                    grado = "2° Básico",
                    curso = "2° Básico A"
                )
            )
            usuarioDao.insertarUsuarios(usuarios)

            // 2. Las 4 Asignaturas oficiales de 2° Básico
            val asignaturas = listOf(
                AsignaturaEntity(
                    id = "asig_lenguaje",
                    nombre = "Lenguaje y Comunicación",
                    codigo = "LEN",
                    descripcion = "Lectura guiada, comprensión y vocabulario para 2° Básico.",
                    colorHex = 0xFF1976D2,
                    nombreIcono = "MenuBook"
                ),
                AsignaturaEntity(
                    id = "asig_matematica",
                    nombre = "Matemática",
                    codigo = "MAT",
                    descripcion = "Conteo progresivo, patrones y resolución de problemas simples.",
                    colorHex = 0xFF388E3C,
                    nombreIcono = "Calculate"
                ),
                AsignaturaEntity(
                    id = "asig_ciencias",
                    nombre = "Ciencias Naturales",
                    codigo = "CNAT",
                    descripcion = "Exploración de seres vivos, estaciones del año y medioambiente.",
                    colorHex = 0xFFF57C00,
                    nombreIcono = "Eco"
                ),
                AsignaturaEntity(
                    id = "asig_historia",
                    nombre = "Historia, Geografía y Ciencias Sociales",
                    codigo = "HIST",
                    descripcion = "Ubicación temporal, secuencias del día y vida cotidiana familiar.",
                    colorHex = 0xFF7B1FA2,
                    nombreIcono = "HistoryEdu"
                )
            )
            asignaturaDao.insertarAsignaturas(asignaturas)

            // 3. Objetivos de Aprendizaje (OA) Mineduc
            val objetivos = listOf(
                ObjetivoAprendizajeEntity(
                    id = "obj_len_04",
                    idAsignatura = "asig_lenguaje",
                    codigo = "OA 04",
                    titulo = "Lectura comprensiva de cuentos breves",
                    descripcion = "Leer independientemente y comprender textos narrativos sencillos identificando personajes y acciones principales.",
                    tituloUnidad = "Unidad 1: Mundos de Papel",
                    ejeCurricular = "Lectura y Comprensión",
                    textoReferencia = "Guía didáctica Mineduc 2° Básico - Narrativa Infantil",
                    estado = EstadoObjetivo.AUTORIZADO,
                    responsableUtp = "Jefatura UTP Marcela Paz",
                    fechaCreacion = "2026-03-10",
                    estaAsignadoAlCurso = true,
                    cursoAsignado = "2° Básico A",
                    intentosMaximos = 3,
                    fechaInicioDisponibilidad = "2026-03-15",
                    fechaFinDisponibilidad = "2026-04-30"
                ),
                ObjetivoAprendizajeEntity(
                    id = "obj_mat_01",
                    idAsignatura = "asig_matematica",
                    codigo = "OA 01",
                    titulo = "Conteo de 2 en 2 y de 5 en 5",
                    descripcion = "Contar colecciones de objetos hasta 50 de 2 en 2 y de 5 en 5 hacia adelante utilizando apoyos visuales concretos.",
                    tituloUnidad = "Unidad 1: Descubriendo los Números",
                    ejeCurricular = "Números y Operaciones",
                    textoReferencia = "Cuaderno de Actividades Matemática 2°",
                    estado = EstadoObjetivo.AUTORIZADO,
                    responsableUtp = "Jefatura UTP Marcela Paz",
                    fechaCreacion = "2026-03-12",
                    estaAsignadoAlCurso = true,
                    cursoAsignado = "2° Básico A",
                    intentosMaximos = 2,
                    fechaInicioDisponibilidad = "2026-03-15",
                    fechaFinDisponibilidad = "2026-04-30"
                ),
                ObjetivoAprendizajeEntity(
                    id = "obj_cnat_08",
                    idAsignatura = "asig_ciencias",
                    codigo = "OA 08",
                    titulo = "Las cuatro estaciones del año",
                    descripcion = "Reconocer las características de las estaciones del año y sus efectos sobre la vestimenta y la naturaleza.",
                    tituloUnidad = "Unidad 2: Nuestro Entorno Vivo",
                    ejeCurricular = "Ciencias de la Tierra y el Universo",
                    textoReferencia = "Mineduc Ciencias 2° - Módulo Clima y Estaciones",
                    estado = EstadoObjetivo.PUBLICADO,
                    responsableUtp = "Jefatura UTP Marcela Paz",
                    fechaCreacion = "2026-03-14",
                    estaAsignadoAlCurso = true,
                    cursoAsignado = "2° Básico A",
                    intentosMaximos = 2,
                    fechaInicioDisponibilidad = "2026-03-20",
                    fechaFinDisponibilidad = "2026-05-15"
                ),
                ObjetivoAprendizajeEntity(
                    id = "obj_hist_02",
                    idAsignatura = "asig_historia",
                    codigo = "OA 02",
                    titulo = "Secuencias temporales: antes y después",
                    descripcion = "Secuenciar acontecimientos de la rutina diaria usando conceptos temporales: antes, ahora, después, ayer y hoy.",
                    tituloUnidad = "Unidad 1: Mi Historia y mi Familia",
                    ejeCurricular = "Historia y Tiempo",
                    textoReferencia = "Texto escolar Historia y Geografía 2° Básico",
                    estado = EstadoObjetivo.EN_REVISION,
                    responsableUtp = "Jefatura UTP Marcela Paz",
                    fechaCreacion = "2026-03-18",
                    estaAsignadoAlCurso = false,
                    cursoAsignado = "2° Básico A",
                    intentosMaximos = 2,
                    fechaInicioDisponibilidad = "2026-04-01",
                    fechaFinDisponibilidad = "2026-05-30"
                )
            )
            objetivoDao.insertarObjetivos(objetivos)

            // 4. Contenidos didácticos con soporte sonoro
            val contenidos = listOf(
                ContenidoDidacticoEntity(
                    id = "cont_len_01",
                    idObjetivo = "obj_len_04",
                    titulo = "El Conejo Gaspar y su Aventura",
                    explicacionEscrita = "Había una vez un pequeño conejo llamado Gaspar. A Gaspar le encantaba saltar por el huerto y compartir zanahorias crujientes con su amiga la ardilla Tina. Siempre que Tina tenía hambre, Gaspar le llevaba dos zanahorias grandes.",
                    guionAudio = "¡Hola amigo! Escucha con atención este lindo cuento: Había una vez un pequeño conejo llamado Gaspar. A Gaspar le encantaba saltar por el huerto y compartir zanahorias con su amiga Tina.",
                    descripcionApoyoVisual = "Un conejo blanco feliz en un prado con zanahorias anaranjadas.",
                    iconoApoyoVisual = "Pets",
                    ejemploGuiado = "Recuerda: Los personajes son quienes viven la historia. Aquí los personajes son el conejo Gaspar y la ardilla Tina.",
                    instrucciones = "Lee o escucha el cuento con el botón de voz. Luego presiona 'Comenzar Actividades'.",
                    ordenSecuencia = 1
                ),
                ContenidoDidacticoEntity(
                    id = "cont_mat_01",
                    idObjetivo = "obj_mat_01",
                    titulo = "¡Saltando de 2 en 2!",
                    explicacionEscrita = "Contar de 2 en 2 es como dar saltitos de rana dejando un número en el medio. Mira la secuencia: 2, 4, 6, 8, 10... Cada vez que saltas, ¡sumas dos elementos más!",
                    guionAudio = "¡Aprendamos a contar saltando! Cuando contamos de 2 en 2, sumamos dos a cada número. Escucha: 2, 4, 6, 8, 10. ¡Es muy fácil y divertido!",
                    descripcionApoyoVisual = "Una ranita saltando sobre piedras con números pares del 2 al 10.",
                    iconoApoyoVisual = "LooksTwo",
                    ejemploGuiado = "Si tienes 4 manzanas y te regalan 2 más, cuentas hacia adelante: 5 y 6. ¡Tienes 6 manzanas en total!",
                    instrucciones = "Observa los números y escucha la explicación presionando el botón parlante.",
                    ordenSecuencia = 1
                ),
                ContenidoDidacticoEntity(
                    id = "cont_cnat_01",
                    idObjetivo = "obj_cnat_08",
                    titulo = "El Otoño y las Hojas Doradas",
                    explicacionEscrita = "En otoño los días se vuelven más frescos y las hojas de los árboles cambian de color a amarillo y marrón antes de caer al suelo. Las personas usamos chaquetas livianas.",
                    guionAudio = "¡Hola explorador! En la estación de otoño, el clima comienza a enfriarse. Los árboles sueltan sus hojas y nosotros usamos abrigos para no resfriarnos.",
                    descripcionApoyoVisual = "Árbol con hojas secas cayendo suavemente por el viento fresco.",
                    iconoApoyoVisual = "WbSunny",
                    ejemploGuiado = "Fíjate cuando salgas al recreo: ¿las hojas están verdes o amarillas?",
                    instrucciones = "Escucha la descripción del otoño para responder los desafíos a continuación.",
                    ordenSecuencia = 1
                )
            )
            contenidoDao.insertarContenidos(contenidos)

            // 5. Desafíos Progresivos
            val desafios = listOf(
                DesafioEntity(
                    id = "chal_len_01",
                    idObjetivo = "obj_len_04",
                    titulo = "Desafío 1: ¿Quién es el personaje?",
                    tipo = TipoDesafio.SELECCION_MULTIPLE,
                    enunciado = "¿Cómo se llamaba el conejo que saltaba por el huerto?",
                    audioEnunciado = "¿Cómo se llamaba el conejo que saltaba por el huerto? Toca la respuesta correcta.",
                    opciones = listOf("Gaspar", "Simón", "Mateo"),
                    indiceCorrecto = 0,
                    explicacionRetroalimentacion = "¡Muy bien! El conejo protagonista de la historia se llama Gaspar.",
                    dificultad = "Fácil",
                    ordenSecuencia = 1
                ),
                DesafioEntity(
                    id = "chal_len_02",
                    idObjetivo = "obj_len_04",
                    titulo = "Desafío 2: ¿Qué alimento compartía?",
                    tipo = TipoDesafio.SELECCION_MULTIPLE,
                    enunciado = "¿Qué comida compartía Gaspar con su amiga la ardilla Tina?",
                    audioEnunciado = "¿Qué comida compartía Gaspar con su amiga la ardilla Tina?",
                    opciones = listOf("Manzanas rojas", "Zanahorias crujientes", "Nueces"),
                    indiceCorrecto = 1,
                    explicacionRetroalimentacion = "¡Excelente! Gaspar compartía ricas zanahorias crujientes.",
                    dificultad = "Medio",
                    ordenSecuencia = 2
                ),
                DesafioEntity(
                    id = "chal_mat_01",
                    idObjetivo = "obj_mat_01",
                    titulo = "Desafío 1: El número que falta",
                    tipo = TipoDesafio.SELECCION_MULTIPLE,
                    enunciado = "Completa la serie de 2 en 2: 2 - 4 - 6 - ¿?",
                    audioEnunciado = "Completa la serie contando de dos en dos: dos, cuatro, seis... ¿cuál viene?",
                    opciones = listOf("7", "8", "9"),
                    indiceCorrecto = 1,
                    explicacionRetroalimentacion = "¡Fantástico! Después del 6 viene el 8 sumando 2.",
                    dificultad = "Fácil",
                    ordenSecuencia = 1
                ),
                DesafioEntity(
                    id = "chal_mat_02",
                    idObjetivo = "obj_mat_01",
                    titulo = "Desafío 2: Continúa la serie",
                    tipo = TipoDesafio.SELECCION_MULTIPLE,
                    enunciado = "Si estás en el 10 y sumas 2, ¿a qué número llegas?",
                    audioEnunciado = "Si estás en el diez y sumas dos, ¿a qué número llegas?",
                    opciones = listOf("11", "12", "14"),
                    indiceCorrecto = 1,
                    explicacionRetroalimentacion = "¡Correcto! Diez más dos es igual a doce.",
                    dificultad = "Medio",
                    ordenSecuencia = 2
                )
            )
            desafioDao.insertarDesafios(desafios)

            // 6. Preguntas de la Evaluación Final
            val preguntas = listOf(
                PreguntaEvaluacionEntity(
                    id = "ev_len_01",
                    idObjetivo = "obj_len_04",
                    numeroPregunta = 1,
                    textoPregunta = "¿Quién es la amiga del conejo Gaspar?",
                    audioPregunta = "Pregunta uno: ¿Quién es la amiga del conejo Gaspar?",
                    opciones = listOf("La ardilla Tina", "La tortuga Rosa", "La paloma Paz"),
                    indiceCorrecto = 0,
                    puntaje = 10,
                    explicacion = "Tina es la ardilla que comparte con Gaspar."
                ),
                PreguntaEvaluacionEntity(
                    id = "ev_len_02",
                    idObjetivo = "obj_len_04",
                    numeroPregunta = 2,
                    textoPregunta = "¿Cuál es el valor principal que muestra Gaspar?",
                    audioPregunta = "Pregunta dos: ¿Cuál es el valor principal que muestra Gaspar?",
                    opciones = listOf("Enojo", "Compartir y amistad", "Dormir todo el día"),
                    indiceCorrecto = 1,
                    puntaje = 10,
                    explicacion = "Gaspar demuestra generosidad al compartir su comida."
                ),
                PreguntaEvaluacionEntity(
                    id = "ev_mat_01",
                    idObjetivo = "obj_mat_01",
                    numeroPregunta = 1,
                    textoPregunta = "¿Cuál es el siguiente número en: 12 - 14 - 16 - ...?",
                    audioPregunta = "Pregunta uno: ¿Cuál es el siguiente número en: doce, catorce, dieciséis?",
                    opciones = listOf("17", "18", "20"),
                    indiceCorrecto = 1,
                    puntaje = 10,
                    explicacion = "16 + 2 = 18."
                ),
                PreguntaEvaluacionEntity(
                    id = "ev_mat_02",
                    idObjetivo = "obj_mat_01",
                    numeroPregunta = 2,
                    textoPregunta = "Si cuentas hacia atrás desde 8 de 2 en 2, ¿qué número sigue?",
                    audioPregunta = "Pregunta dos: Si cuentas hacia atrás desde ocho de dos en dos, ¿qué número sigue?",
                    opciones = listOf("6", "7", "5"),
                    indiceCorrecto = 0,
                    puntaje = 10,
                    explicacion = "Restando 2 a 8 obtenemos 6."
                )
            )
            preguntaEvaluacionDao.insertarPreguntasEvaluacion(preguntas)

            // 7. Progreso inicial del estudiante
            val progresos = listOf(
                ProgresoEstudianteEntity(
                    id = "prog_lucas_len",
                    idEstudiante = "usr_estudiante_1",
                    idObjetivo = "obj_len_04",
                    contenidoCompletado = true,
                    desafiosCompletados = 2,
                    totalDesafios = 2,
                    intentosUsados = 1,
                    ultimoPuntaje = 20,
                    puntajeMaximo = 20,
                    fechaUltimoIntento = "2026-03-22 10:15",
                    porcentaje = 100,
                    textoEstado = "Aprobado con Distinción"
                ),
                ProgresoEstudianteEntity(
                    id = "prog_sofia_len",
                    idEstudiante = "usr_estudiante_2",
                    idObjetivo = "obj_len_04",
                    contenidoCompletado = true,
                    desafiosCompletados = 1,
                    totalDesafios = 2,
                    intentosUsados = 1,
                    ultimoPuntaje = 10,
                    puntajeMaximo = 20,
                    fechaUltimoIntento = "2026-03-22 11:30",
                    porcentaje = 50,
                    textoEstado = "Requiere Refuerzo"
                )
            )
            for (p in progresos) {
                progresoDao.insertarProgreso(p)
            }

            // 8. Informes Pedagógicos consolidados para el docente
            val informes = listOf(
                InformePedagogicoEntity(
                    id = "rep_lucas_len",
                    idEstudiante = "usr_estudiante_1",
                    nombreEstudiante = "Lucas Martínez",
                    nombreCurso = "2° Básico A",
                    nombreAsignatura = "Lenguaje y Comunicación",
                    codigoObjetivo = "OA 04",
                    tituloObjetivo = "Lectura comprensiva de cuentos breves",
                    puntajeGlobal = 20,
                    puntajeMaximo = 20,
                    porcentaje = 100,
                    fortalezas = "Excelente comprensión lectora, reconoce personajes y secuencias con rapidez.",
                    areasRefuerzo = "Continuar fortaleciendo lectura en voz alta fluida.",
                    observacionDocente = "Demuestra gran autonomía y entusiasmo al resolver los desafíos didácticos.",
                    fechaEmision = "2026-03-22 10:20",
                    estaValidadoPorDocente = true
                ),
                InformePedagogicoEntity(
                    id = "rep_sofia_len",
                    idEstudiante = "usr_estudiante_2",
                    nombreEstudiante = "Sofía Morales",
                    nombreCurso = "2° Básico A",
                    nombreAsignatura = "Lenguaje y Comunicación",
                    codigoObjetivo = "OA 04",
                    tituloObjetivo = "Lectura comprensiva de cuentos breves",
                    puntajeGlobal = 10,
                    puntajeMaximo = 20,
                    porcentaje = 50,
                    fortalezas = "Identifica al personaje principal de la historia.",
                    areasRefuerzo = "Requiere mayor apoyo en la retención de detalles secundarios y vocabulario.",
                    observacionDocente = "",
                    fechaEmision = "2026-03-22 11:35",
                    estaValidadoPorDocente = false
                )
            )
            informeDao.insertarInformes(informes)
        }
    }
}
