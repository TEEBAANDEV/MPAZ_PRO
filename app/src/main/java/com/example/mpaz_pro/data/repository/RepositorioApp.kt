package com.example.mpaz_pro.data.repository

import com.example.mpaz_pro.data.local.AppDao
import com.example.mpaz_pro.data.local.InformeEntity
import com.example.mpaz_pro.data.local.ObjetivoEntity
import com.example.mpaz_pro.data.local.UsuarioEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio centralizado que conecta los ViewModels con el DAO de Room.
 */
class RepositorioApp(private val dao: AppDao) {

    suspend fun iniciarSesion(usuario: String, clave: String): UsuarioEntity? {
        return dao.iniciarSesion(usuario, clave)
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
}
