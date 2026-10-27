package com.example.mpaz_pro.data.repository

import com.example.mpaz_pro.data.local.AppDao
import com.example.mpaz_pro.data.local.InformeEntity
import com.example.mpaz_pro.data.local.NotaEntity
import com.example.mpaz_pro.data.local.ObjetivoEntity
import com.example.mpaz_pro.data.local.UsuarioEntity
import kotlinx.coroutines.flow.Flow

class RepositorioApp(private val dao: AppDao) {

    suspend fun iniciarSesion(usuario: String, clave: String): UsuarioEntity? {
        return dao.iniciarSesion(usuario, clave)
    }

    fun obtenerUsuarioPorNombre(nombreUsuario: String): Flow<UsuarioEntity?> {
        return dao.obtenerUsuarioPorNombre(nombreUsuario)
    }

    suspend fun actualizarFotoPerfil(nombreUsuario: String, uri: String?) {
        dao.actualizarFotoPerfil(nombreUsuario, uri)
    }

    fun obtenerTodosLosObjetivos(): Flow<List<ObjetivoEntity>> {
        return dao.obtenerTodosLosObjetivos()
    }

    fun obtenerObjetivosAsignados(): Flow<List<ObjetivoEntity>> {
        return dao.obtenerObjetivosAsignados()
    }

    suspend fun guardarObjetivo(objetivo: ObjetivoEntity) {
        dao.guardarObjetivo(objetivo)
    }

    suspend fun actualizarEstadoObjetivo(id: String, nuevoEstado: String) {
        dao.actualizarEstadoObjetivo(id, nuevoEstado)
    }

    suspend fun actualizarAsignacionObjetivo(id: String, asignado: Boolean) {
        dao.actualizarAsignacionObjetivo(id, asignado)
    }

    fun obtenerTodosLosInformes(): Flow<List<InformeEntity>> {
        return dao.obtenerTodosLosInformes()
    }

    suspend fun guardarInforme(informe: InformeEntity) {
        dao.guardarInforme(informe)
    }

    suspend fun actualizarObservacionInforme(id: String, observacion: String) {
        dao.actualizarObservacionInforme(id, observacion)
    }

    fun obtenerTodasLasNotas(): Flow<List<NotaEntity>> {
        return dao.obtenerTodasLasNotas()
    }

    fun obtenerNotasPorEstudiante(idEstudiante: String): Flow<List<NotaEntity>> {
        return dao.obtenerNotasPorEstudiante(idEstudiante)
    }

    suspend fun guardarNota(nota: NotaEntity) {
        dao.guardarNota(nota)
    }

    suspend fun eliminarNota(id: String) {
        dao.eliminarNota(id)
    }
}
