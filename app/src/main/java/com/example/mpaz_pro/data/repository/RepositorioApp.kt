package com.example.mpaz_pro.data.repository

import com.example.mpaz_pro.data.local.BaseDatosApp
import com.example.mpaz_pro.data.local.entity.*
import kotlinx.coroutines.flow.Flow

class RepositorioApp(private val baseDatos: BaseDatosApp) {

    // Autenticación y Usuarios
    suspend fun iniciarSesion(nombreUsuario: String, clave: String): UsuarioEntity? =
        baseDatos.usuarioDao().iniciarSesion(nombreUsuario, clave)

    suspend fun obtenerUsuarioPorId(idUsuario: String): UsuarioEntity? =
        baseDatos.usuarioDao().obtenerUsuarioPorId(idUsuario)

    fun obtenerUsuariosPorRol(rol: RolUsuario): Flow<List<UsuarioEntity>> =
        baseDatos.usuarioDao().obtenerUsuariosPorRol(rol)

    // Asignaturas
    fun obtenerTodasLasAsignaturas(): Flow<List<AsignaturaEntity>> =
        baseDatos.asignaturaDao().obtenerTodasLasAsignaturas()

    suspend fun obtenerAsignaturaPorId(id: String): AsignaturaEntity? =
        baseDatos.asignaturaDao().obtenerAsignaturaPorId(id)

    // Objetivos de Aprendizaje (UTP y Docente)
    fun obtenerTodosLosObjetivos(): Flow<List<ObjetivoAprendizajeEntity>> =
        baseDatos.objetivoAprendizajeDao().obtenerTodosLosObjetivos()

    fun obtenerObjetivosPorAsignatura(idAsignatura: String): Flow<List<ObjetivoAprendizajeEntity>> =
        baseDatos.objetivoAprendizajeDao().obtenerObjetivosPorAsignatura(idAsignatura)

    suspend fun obtenerObjetivoPorId(id: String): ObjetivoAprendizajeEntity? =
        baseDatos.objetivoAprendizajeDao().obtenerObjetivoPorId(id)

    fun obtenerObjetivosAutorizados(): Flow<List<ObjetivoAprendizajeEntity>> =
        baseDatos.objetivoAprendizajeDao().obtenerObjetivosAutorizados()

    fun obtenerObjetivosAsignadosParaEstudiantes(): Flow<List<ObjetivoAprendizajeEntity>> =
        baseDatos.objetivoAprendizajeDao().obtenerObjetivosAsignadosParaEstudiantes()

    suspend fun insertarObjetivo(objetivo: ObjetivoAprendizajeEntity) =
        baseDatos.objetivoAprendizajeDao().insertarObjetivo(objetivo)

    suspend fun actualizarObjetivo(objetivo: ObjetivoAprendizajeEntity) =
        baseDatos.objetivoAprendizajeDao().actualizarObjetivo(objetivo)

    suspend fun actualizarEstadoObjetivo(id: String, estado: EstadoObjetivo) =
        baseDatos.objetivoAprendizajeDao().actualizarEstadoObjetivo(id, estado)

    suspend fun actualizarAsignacion(id: String, estaAsignado: Boolean, intentosMaximos: Int, fechaInicio: String, fechaFin: String) =
        baseDatos.objetivoAprendizajeDao().actualizarAsignacion(id, estaAsignado, intentosMaximos, fechaInicio, fechaFin)

    // Contenidos Didácticos
    fun obtenerContenidosPorObjetivo(idObjetivo: String): Flow<List<ContenidoDidacticoEntity>> =
        baseDatos.contenidoDidacticoDao().obtenerContenidosPorObjetivo(idObjetivo)

    suspend fun obtenerPrimerContenidoPorObjetivo(idObjetivo: String): ContenidoDidacticoEntity? =
        baseDatos.contenidoDidacticoDao().obtenerPrimerContenidoPorObjetivo(idObjetivo)

    suspend fun insertarContenido(contenido: ContenidoDidacticoEntity) =
        baseDatos.contenidoDidacticoDao().insertarContenido(contenido)

    // Desafíos
    fun obtenerDesafiosPorObjetivo(idObjetivo: String): Flow<List<DesafioEntity>> =
        baseDatos.desafioDao().obtenerDesafiosPorObjetivo(idObjetivo)

    // Preguntas de Evaluación Final
    fun obtenerPreguntasPorObjetivo(idObjetivo: String): Flow<List<PreguntaEvaluacionEntity>> =
        baseDatos.preguntaEvaluacionDao().obtenerPreguntasPorObjetivo(idObjetivo)

    // Progreso del Estudiante
    fun obtenerProgresoPorEstudiante(idEstudiante: String): Flow<List<ProgresoEstudianteEntity>> =
        baseDatos.progresoEstudianteDao().obtenerProgresoPorEstudiante(idEstudiante)

    suspend fun obtenerProgresoPorObjetivo(idEstudiante: String, idObjetivo: String): ProgresoEstudianteEntity? =
        baseDatos.progresoEstudianteDao().obtenerProgresoPorObjetivo(idEstudiante, idObjetivo)

    fun obtenerTodosLosProgresos(): Flow<List<ProgresoEstudianteEntity>> =
        baseDatos.progresoEstudianteDao().obtenerTodosLosProgresos()

    suspend fun guardarProgreso(progreso: ProgresoEstudianteEntity) =
        baseDatos.progresoEstudianteDao().insertarProgreso(progreso)

    // Informes Pedagógicos
    fun obtenerTodosLosInformes(): Flow<List<InformePedagogicoEntity>> =
        baseDatos.informePedagogicoDao().obtenerTodosLosInformes()

    suspend fun obtenerInformePorId(idInforme: String): InformePedagogicoEntity? =
        baseDatos.informePedagogicoDao().obtenerInformePorId(idInforme)

    fun obtenerInformesPorEstudiante(idEstudiante: String): Flow<List<InformePedagogicoEntity>> =
        baseDatos.informePedagogicoDao().obtenerInformesPorEstudiante(idEstudiante)

    suspend fun guardarInforme(informe: InformePedagogicoEntity) =
        baseDatos.informePedagogicoDao().insertarInforme(informe)

    suspend fun actualizarObservacionDocente(idInforme: String, observacion: String) =
        baseDatos.informePedagogicoDao().actualizarObservacionDocente(idInforme, observacion)
}
