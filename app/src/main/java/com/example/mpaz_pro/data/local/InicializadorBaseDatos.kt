package com.example.mpaz_pro.data.local

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object InicializadorBaseDatos {

    suspend fun inicializar(dao: AppDao) = withContext(Dispatchers.IO) {

        val usuarios = listOf(
            UsuarioEntity("utp", "1234", "Jefatura UTP - Marcela Paz", "UTP"),
            UsuarioEntity("docente", "1234", "Prof. Mario Silva", "DOCENTE"),
            UsuarioEntity("lucas", "1234", "Lucas González", "ESTUDIANTE")
        )
        dao.insertarUsuarios(usuarios)

        val objetivos = listOf(
            ObjetivoEntity(
                id = "obj_lenguaje_01",
                idAsignatura = "lenguaje",
                nombreAsignatura = "Lenguaje y Comunicación",
                codigo = "OA 04",
                titulo = "Lectura de Cuentos Breves",
                descripcion = "Había una vez un pequeño zorrito chileno que vivía en la cordillera. Cada mañana saludaba al sol y ayudaba a sus amigos del bosque a encontrar semillas.",
                pregunta = "¿Dónde vivía el pequeño zorrito del cuento?",
                opciones = "En la cordillera,En la playa,En la ciudad",
                indiceCorrecto = 0,
                estado = "AUTORIZADO",
                asignadoAlCurso = true
            ),
            ObjetivoEntity(
                id = "obj_matematica_01",
                idAsignatura = "matematica",
                nombreAsignatura = "Matemática",
                codigo = "OA 09",
                titulo = "Adición y Sustracción hasta el 100",
                descripcion = "En la sala de 2° básico, Sofía tiene 15 lápices de colores y su compañero le regala 10 lápices más para dibujar.",
                pregunta = "¿Cuántos lápices tiene Sofía en total?",
                opciones = "20 lápices,25 lápices,30 lápices",
                indiceCorrecto = 1,
                estado = "AUTORIZADO",
                asignadoAlCurso = true
            ),
            ObjetivoEntity(
                id = "obj_ciencias_01",
                idAsignatura = "ciencias",
                nombreAsignatura = "Ciencias Naturales",
                codigo = "OA 02",
                titulo = "Animales Nativos de Chile",
                descripcion = "El huemul y el cóndor andino son animales protegidos en nuestro país. El cóndor vuela alto en la cordillera y el huemul camina por los bosques del sur.",
                pregunta = "¿Cuál de estos animales vuela en la cordillera?",
                opciones = "El cóndor andino,El pudú,El delfín chileno",
                indiceCorrecto = 0,
                estado = "AUTORIZADO",
                asignadoAlCurso = true
            ),
            ObjetivoEntity(
                id = "obj_historia_01",
                idAsignatura = "historia",
                nombreAsignatura = "Historia y Geografía",
                codigo = "OA 01",
                titulo = "Pueblos Originarios y Tradiciones",
                descripcion = "En Chile celebramos nuestras fiestas tradicionales con bailes típicos como la cueca y platos tradicionales como las ricas empanadas chilenas.",
                pregunta = "¿Cuál es nuestro baile típico tradicional?",
                opciones = "El tango,La cueca,La salsa",
                indiceCorrecto = 1,
                estado = "AUTORIZADO",
                asignadoAlCurso = true
            )
        )
        dao.insertarObjetivos(objetivos)

        val informes = listOf(
            InformeEntity(
                id = "inf_01",
                idEstudiante = "lucas",
                nombreEstudiante = "Lucas González",
                curso = "2° Básico A",
                asignatura = "Lenguaje y Comunicación",
                porcentajeLogro = 85,
                desafiosCompletados = 3,
                observacionDocente = "Excelente comprensión lectora apoyada en la lectura en voz alta.",
                fecha = "13/10/2026"
            ),
            InformeEntity(
                id = "inf_02",
                idEstudiante = "lucas",
                nombreEstudiante = "Lucas González",
                curso = "2° Básico A",
                asignatura = "Matemática",
                porcentajeLogro = 90,
                desafiosCompletados = 4,
                observacionDocente = "Gran desempeño en cálculo mental y resolución de problemas.",
                fecha = "13/10/2026"
            )
        )
        dao.insertarInformes(informes)
    }
}
