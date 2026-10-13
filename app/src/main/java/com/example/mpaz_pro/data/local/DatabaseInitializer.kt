package com.example.mpaz_pro.data.local

import com.example.mpaz_pro.data.local.dao.*
import com.example.mpaz_pro.data.local.entity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object DatabaseInitializer {

    suspend fun populateInitialData(database: AppDatabase) {
        withContext(Dispatchers.IO) {
            val userDao = database.userDao()
            val subjectDao = database.subjectDao()
            val objectiveDao = database.learningObjectiveDao()
            val contentDao = database.contentDao()
            val challengeDao = database.challengeDao()
            val examQuestionDao = database.examQuestionDao()
            val progressDao = database.studentProgressDao()
            val reportDao = database.pedagogicalReportDao()

            // 1. Usuarios de prueba (Ficticios para cumplimiento de normas académicas)
            val users = listOf(
                UserEntity(
                    id = "user_utp",
                    username = "utp",
                    passwordHash = "1234",
                    fullName = "Prof. Claudia Reyes (Jefatura UTP)",
                    role = UserRole.UTP,
                    grade = "Primer Ciclo",
                    course = "Colegio Marcela Paz"
                ),
                UserEntity(
                    id = "user_docente",
                    username = "docente",
                    passwordHash = "1234",
                    fullName = "Prof. Mario Silva",
                    role = UserRole.DOCENTE,
                    grade = "2° Básico",
                    course = "2° Básico A"
                ),
                UserEntity(
                    id = "user_estudiante_1",
                    username = "lucas",
                    passwordHash = "1234",
                    fullName = "Lucas Martínez",
                    role = UserRole.ESTUDIANTE,
                    grade = "2° Básico",
                    course = "2° Básico A"
                ),
                UserEntity(
                    id = "user_estudiante_2",
                    username = "sofia",
                    passwordHash = "1234",
                    fullName = "Sofía Morales",
                    role = UserRole.ESTUDIANTE,
                    grade = "2° Básico",
                    course = "2° Básico A"
                )
            )
            userDao.insertUsers(users)

            // 2. Las 4 Asignaturas oficiales de 2° Básico
            val subjects = listOf(
                SubjectEntity(
                    id = "sub_lenguaje",
                    name = "Lenguaje y Comunicación",
                    code = "LEN",
                    description = "Lectura guiada, comprensión y vocabulario para 2° Básico.",
                    colorHex = 0xFF1976D2, // Azul educativo
                    iconName = "MenuBook"
                ),
                SubjectEntity(
                    id = "sub_matematica",
                    name = "Matemática",
                    code = "MAT",
                    description = "Conteo progresivo, patrones y resolución de problemas simples.",
                    colorHex = 0xFF388E3C, // Verde amigable
                    iconName = "Calculate"
                ),
                SubjectEntity(
                    id = "sub_ciencias",
                    name = "Ciencias Naturales",
                    code = "CNAT",
                    description = "Exploración de seres vivos, estaciones del año y medioambiente.",
                    colorHex = 0xFFF57C00, // Naranja vibrante
                    iconName = "Eco"
                ),
                SubjectEntity(
                    id = "sub_historia",
                    name = "Historia, Geografía y Ciencias Sociales",
                    code = "HIST",
                    description = "Ubicación temporal, secuencias del día y vida cotidiana familiar.",
                    colorHex = 0xFF7B1FA2, // Púrpura suave
                    iconName = "HistoryEdu"
                )
            )
            subjectDao.insertSubjects(subjects)

            // 3. Objetivos de Aprendizaje (OA)
            val objectives = listOf(
                LearningObjectiveEntity(
                    id = "obj_len_04",
                    subjectId = "sub_lenguaje",
                    code = "OA 04",
                    title = "Lectura comprensiva de cuentos breves",
                    description = "Leer independientemente y comprender textos narrativos sencillos identificando personajes y acciones principales.",
                    unitTitle = "Unidad 1: Mundos de Papel",
                    curriculumAxis = "Lectura y Comprensión",
                    referenceText = "Guía didáctica Mineduc 2° Básico - Narrativa Infantil",
                    status = ObjectiveStatus.AUTORIZADO,
                    utpResponsible = "Jefatura UTP Marcela Paz",
                    creationDate = "2026-03-10",
                    isAssignedToCourse = true,
                    assignedCourse = "2° Básico A",
                    maxAttempts = 3,
                    availabilityStartDate = "2026-03-15",
                    availabilityEndDate = "2026-04-30"
                ),
                LearningObjectiveEntity(
                    id = "obj_mat_01",
                    subjectId = "sub_matematica",
                    code = "OA 01",
                    title = "Conteo de 2 en 2 y de 5 en 5",
                    description = "Contar colecciones de objetos hasta 50 de 2 en 2 y de 5 en 5 hacia adelante utilizando apoyos visuales concretos.",
                    unitTitle = "Unidad 1: Descubriendo los Números",
                    curriculumAxis = "Números y Operaciones",
                    referenceText = "Cuaderno de Actividades Matemática 2°",
                    status = ObjectiveStatus.AUTORIZADO,
                    utpResponsible = "Jefatura UTP Marcela Paz",
                    creationDate = "2026-03-12",
                    isAssignedToCourse = true,
                    assignedCourse = "2° Básico A",
                    maxAttempts = 2,
                    availabilityStartDate = "2026-03-15",
                    availabilityEndDate = "2026-04-30"
                ),
                LearningObjectiveEntity(
                    id = "obj_cnat_08",
                    subjectId = "sub_ciencias",
                    code = "OA 08",
                    title = "Las cuatro estaciones del año",
                    description = "Reconocer las características de las estaciones del año y sus efectos sobre la vestimenta y la naturaleza.",
                    unitTitle = "Unidad 2: Nuestro Entorno Vivo",
                    curriculumAxis = "Ciencias de la Tierra y el Universo",
                    referenceText = "Mineduc Ciencias 2° - Módulo Clima y Estaciones",
                    status = ObjectiveStatus.PUBLICADO,
                    utpResponsible = "Jefatura UTP Marcela Paz",
                    creationDate = "2026-03-14",
                    isAssignedToCourse = true,
                    assignedCourse = "2° Básico A",
                    maxAttempts = 2,
                    availabilityStartDate = "2026-03-20",
                    availabilityEndDate = "2026-05-15"
                ),
                LearningObjectiveEntity(
                    id = "obj_hist_02",
                    subjectId = "sub_historia",
                    code = "OA 02",
                    title = "Secuencias temporales: antes y después",
                    description = "Secuenciar acontecimientos de la rutina diaria usando conceptos temporales: antes, ahora, después, ayer y hoy.",
                    unitTitle = "Unidad 1: Mi Historia y mi Familia",
                    curriculumAxis = "Historia y Tiempo",
                    referenceText = "Texto escolar Historia y Geografía 2° Básico",
                    status = ObjectiveStatus.EN_REVISION, // Permite a UTP aprobarlo
                    utpResponsible = "Jefatura UTP Marcela Paz",
                    creationDate = "2026-03-18",
                    isAssignedToCourse = false,
                    assignedCourse = "2° Básico A",
                    maxAttempts = 2,
                    availabilityStartDate = "2026-04-01",
                    availabilityEndDate = "2026-05-30"
                )
            )
            objectiveDao.insertObjectives(objectives)

            // 4. Contenidos Didácticos con Guiones de Audio (TTS nativo)
            val contents = listOf(
                ContentEntity(
                    id = "cont_len_01",
                    objectiveId = "obj_len_04",
                    title = "El Conejo Saltarín y su Aventura",
                    writtenExplanation = "Había una vez un pequeño conejo llamado Gaspar. A Gaspar le encantaba saltar por el huerto y compartir zanahorias crujientes con su amiga la ardilla Tina. Siempre que Tina tenía hambre, Gaspar le llevaba dos zanahorias grandes.",
                    audioScript = "¡Hola amigo! Escucha con atención este lindo cuento: Había una vez un pequeño conejo llamado Gaspar. A Gaspar le encantaba saltar por el huerto y compartir zanahorias con su amiga Tina.",
                    visualAidDescription = "Un conejo blanco feliz en un prado con zanahorias anaranjadas.",
                    visualAidIcon = "Pets",
                    guidedExample = "Recuerda: Los personajes son quienes viven la historia. Aquí los personajes son el conejo Gaspar y la ardilla Tina.",
                    instructions = "Lee o escucha el cuento con el botón de voz. Luego presiona 'Comenzar Actividades'.",
                    sequenceOrder = 1
                ),
                ContentEntity(
                    id = "cont_mat_01",
                    objectiveId = "obj_mat_01",
                    title = "¡Saltando de 2 en 2!",
                    writtenExplanation = "Contar de 2 en 2 es como dar saltitos de rana dejando un número en el medio. Mira la secuencia: 2, 4, 6, 8, 10... Cada vez que saltas, ¡sumas dos elementos más!",
                    audioScript = "¡Aprendamos a contar saltando! Cuando contamos de 2 en 2, sumamos dos a cada número. Escucha: 2, 4, 6, 8, 10. ¡Es muy fácil y divertido!",
                    visualAidDescription = "Una ranita saltando sobre piedras con números pares del 2 al 10.",
                    visualAidIcon = "LooksTwo",
                    guidedExample = "Si tienes 4 manzanas y te regalan 2 más, cuentas hacia adelante: 5 y 6. ¡Tienes 6 manzanas en total!",
                    instructions = "Observa los números y escucha la explicación presionando el botón parlante.",
                    sequenceOrder = 1
                ),
                ContentEntity(
                    id = "cont_cnat_01",
                    objectiveId = "obj_cnat_08",
                    title = "El Otoño y las Hojas Doradas",
                    writtenExplanation = "En otoño los días se vuelven más frescos y las hojas de los árboles cambian de color a amarillo y marrón antes de caer al suelo. Las personas usamos chaquetas livianas.",
                    audioScript = "¡Hola explorador! En la estación de otoño, el clima comienza a enfriarse. Los árboles sueltan sus hojas y nosotros usamos abrigos para no resfriarnos.",
                    visualAidDescription = "Árbol con hojas secas cayendo suavemente por el viento fresco.",
                    visualAidIcon = "WbSunny",
                    guidedExample = "Fíjate cuando salgas al recreo: ¿las hojas están verdes o amarillas?",
                    instructions = "Escucha la descripción del otoño para responder los desafíos a continuación.",
                    sequenceOrder = 1
                )
            )
            contentDao.insertContents(contents)

            // 5. Desafíos Progresivos (Activities)
            val challenges = listOf(
                ChallengeEntity(
                    id = "chal_len_01",
                    objectiveId = "obj_len_04",
                    title = "Desafío 1: ¿Quién es el personaje?",
                    type = ChallengeType.SELECCION_MULTIPLE,
                    prompt = "¿Cómo se llamaba el conejo que saltaba por el huerto?",
                    audioPrompt = "¿Cómo se llamaba el conejo que saltaba por el huerto? Toca la respuesta correcta.",
                    options = listOf("Gaspar", "Simón", "Mateo"),
                    correctIndex = 0,
                    feedbackExplanation = "¡Muy bien! El conejo protagonista de la historia se llama Gaspar.",
                    difficulty = "Fácil",
                    sequenceOrder = 1
                ),
                ChallengeEntity(
                    id = "chal_len_02",
                    objectiveId = "obj_len_04",
                    title = "Desafío 2: ¿Qué alimento compartía?",
                    type = ChallengeType.SELECCION_MULTIPLE,
                    prompt = "¿Qué comida compartía Gaspar con su amiga la ardilla Tina?",
                    audioPrompt = "¿Qué comida compartía Gaspar con su amiga la ardilla Tina?",
                    options = listOf("Manzanas rojas", "Zanahorias crujientes", "Nueces"),
                    correctIndex = 1,
                    feedbackExplanation = "¡Excelente! Gaspar compartía ricas zanahorias crujientes.",
                    difficulty = "Medio",
                    sequenceOrder = 2
                ),
                ChallengeEntity(
                    id = "chal_mat_01",
                    objectiveId = "obj_mat_01",
                    title = "Desafío 1: El número que falta",
                    type = ChallengeType.SELECCION_MULTIPLE,
                    prompt = "Completa la serie de 2 en 2: 2 - 4 - 6 - ¿?",
                    audioPrompt = "Completa la serie contando de dos en dos: dos, cuatro, seis... ¿cuál viene?",
                    options = listOf("7", "8", "9"),
                    correctIndex = 1,
                    feedbackExplanation = "¡Fantástico! Después del 6 viene el 8 sumando 2.",
                    difficulty = "Fácil",
                    sequenceOrder = 1
                ),
                ChallengeEntity(
                    id = "chal_mat_02",
                    objectiveId = "obj_mat_01",
                    title = "Desafío 2: Continúa la serie",
                    type = ChallengeType.SELECCION_MULTIPLE,
                    prompt = "Si estás en el 10 y sumas 2, ¿a qué número llegas?",
                    audioPrompt = "Si estás en el diez y sumas dos, ¿a qué número llegas?",
                    options = listOf("11", "12", "14"),
                    correctIndex = 1,
                    feedbackExplanation = "¡Correcto! Diez más dos es igual a doce.",
                    difficulty = "Medio",
                    sequenceOrder = 2
                ),
                ChallengeEntity(
                    id = "chal_cnat_01",
                    objectiveId = "obj_cnat_08",
                    title = "Desafío 1: Reconoce la estación",
                    type = ChallengeType.SELECCION_MULTIPLE,
                    prompt = "¿En qué estación se caen las hojas secas de los árboles?",
                    audioPrompt = "¿En qué estación del año se caen las hojas secas de los árboles?",
                    options = listOf("Verano", "Otoño", "Primavera"),
                    correctIndex = 1,
                    feedbackExplanation = "¡Muy bien! En otoño las hojas caen por el cambio de estación.",
                    difficulty = "Fácil",
                    sequenceOrder = 1
                )
            )
            challengeDao.insertChallenges(challenges)

            // 6. Preguntas de la Prueba Final
            val examQuestions = listOf(
                ExamQuestionEntity(
                    id = "exam_len_01",
                    objectiveId = "obj_len_04",
                    questionNumber = 1,
                    questionText = "¿Quién es la amiga del conejo Gaspar?",
                    audioQuestion = "Pregunta uno: ¿Quién es la amiga del conejo Gaspar?",
                    options = listOf("La ardilla Tina", "La tortuga Rosa", "La paloma Paz"),
                    correctIndex = 0,
                    points = 10,
                    explanation = "Tina es la ardilla que comparte con Gaspar."
                ),
                ExamQuestionEntity(
                    id = "exam_len_02",
                    objectiveId = "obj_len_04",
                    questionNumber = 2,
                    questionText = "¿Cuál es el valor principal que muestra Gaspar?",
                    audioQuestion = "Pregunta dos: ¿Cuál es el valor principal que muestra Gaspar?",
                    options = listOf("Enojo", "Compartir y amistad", "Dormir todo el día"),
                    correctIndex = 1,
                    points = 10,
                    explanation = "Gaspar demuestra generosidad al compartir su comida."
                ),
                ExamQuestionEntity(
                    id = "exam_mat_01",
                    objectiveId = "obj_mat_01",
                    questionNumber = 1,
                    questionText = "¿Cuál es el siguiente número en: 12 - 14 - 16 - ...?",
                    audioQuestion = "Pregunta uno: ¿Cuál es el siguiente número en: doce, catorce, dieciséis?",
                    options = listOf("17", "18", "20"),
                    correctIndex = 1,
                    points = 10,
                    explanation = "16 + 2 = 18."
                ),
                ExamQuestionEntity(
                    id = "exam_mat_02",
                    objectiveId = "obj_mat_01",
                    questionNumber = 2,
                    questionText = "Si cuentas hacia atrás desde 8 de 2 en 2, ¿qué número sigue?",
                    audioQuestion = "Pregunta dos: Si cuentas hacia atrás desde ocho de dos en dos, ¿qué número sigue?",
                    options = listOf("6", "7", "5"),
                    correctIndex = 0,
                    points = 10,
                    explanation = "Restando 2 a 8 obtenemos 6."
                )
            )
            examQuestionDao.insertExamQuestions(examQuestions)

            // 7. Progreso inicial del estudiante (simulado para demostración docente)
            val progresses = listOf(
                StudentProgressEntity(
                    id = "prog_lucas_len",
                    studentId = "user_estudiante_1",
                    objectiveId = "obj_len_04",
                    completedContent = true,
                    completedChallengesCount = 2,
                    totalChallengesCount = 2,
                    attemptsUsed = 1,
                    lastScore = 20,
                    maxScore = 20,
                    lastAttemptDate = "2026-03-22 10:15",
                    percentage = 100,
                    statusText = "Aprobado con Distinción"
                ),
                StudentProgressEntity(
                    id = "prog_sofia_len",
                    studentId = "user_estudiante_2",
                    objectiveId = "obj_len_04",
                    completedContent = true,
                    completedChallengesCount = 1,
                    totalChallengesCount = 2,
                    attemptsUsed = 1,
                    lastScore = 10,
                    maxScore = 20,
                    lastAttemptDate = "2026-03-22 11:30",
                    percentage = 50,
                    statusText = "Requiere Refuerzo"
                )
            )
            for (p in progresses) {
                progressDao.insertProgress(p)
            }

            // 8. Informes Pedagógicos consolidados para el docente
            val reports = listOf(
                PedagogicalReportEntity(
                    id = "rep_lucas_len",
                    studentId = "user_estudiante_1",
                    studentName = "Lucas Martínez",
                    courseName = "2° Básico A",
                    subjectName = "Lenguaje y Comunicación",
                    objectiveCode = "OA 04",
                    objectiveTitle = "Lectura comprensiva de cuentos breves",
                    globalScore = 20,
                    maxScore = 20,
                    percentage = 100,
                    strengths = "Excelente comprensión lectora, reconoce personajes y secuencias con rapidez.",
                    reinforcementAreas = "Continuar fortaleciendo lectura en voz alta fluida.",
                    teacherObservation = "Demuestra gran autonomía y entusiasmo al resolver los desafíos didácticos.",
                    emissionDate = "2026-03-22 10:20",
                    isValidatedByTeacher = true
                ),
                PedagogicalReportEntity(
                    id = "rep_sofia_len",
                    studentId = "user_estudiante_2",
                    studentName = "Sofía Morales",
                    courseName = "2° Básico A",
                    subjectName = "Lenguaje y Comunicación",
                    objectiveCode = "OA 04",
                    objectiveTitle = "Lectura comprensiva de cuentos breves",
                    globalScore = 10,
                    maxScore = 20,
                    percentage = 50,
                    strengths = "Identifica al personaje principal de la historia.",
                    reinforcementAreas = "Requiere mayor apoyo en la retención de detalles secundarios y vocabulario.",
                    teacherObservation = "",
                    emissionDate = "2026-03-22 11:35",
                    isValidatedByTeacher = false
                )
            )
            reportDao.insertReports(reports)
        }
    }
}
