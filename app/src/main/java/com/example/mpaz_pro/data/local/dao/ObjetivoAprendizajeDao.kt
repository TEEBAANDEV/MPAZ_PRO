package com.example.mpaz_pro.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mpaz_pro.data.local.entity.EstadoObjetivo
import com.example.mpaz_pro.data.local.entity.ObjetivoAprendizajeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ObjetivoAprendizajeDao {
    @Query("SELECT * FROM objetivos_aprendizaje")
    fun obtenerTodosLosObjetivos(): Flow<List<ObjetivoAprendizajeEntity>>

    @Query("SELECT * FROM objetivos_aprendizaje WHERE idAsignatura = :idAsignatura")
    fun obtenerObjetivosPorAsignatura(idAsignatura: String): Flow<List<ObjetivoAprendizajeEntity>>

    @Query("SELECT * FROM objetivos_aprendizaje WHERE id = :id LIMIT 1")
    suspend fun obtenerObjetivoPorId(id: String): ObjetivoAprendizajeEntity?

    @Query("SELECT * FROM objetivos_aprendizaje WHERE estado = 'AUTORIZADO' OR estado = 'PUBLICADO'")
    fun obtenerObjetivosAutorizados(): Flow<List<ObjetivoAprendizajeEntity>>

    @Query("SELECT * FROM objetivos_aprendizaje WHERE estaAsignadoAlCurso = 1 AND (estado = 'AUTORIZADO' OR estado = 'PUBLICADO')")
    fun obtenerObjetivosAsignadosParaEstudiantes(): Flow<List<ObjetivoAprendizajeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarObjetivos(objetivos: List<ObjetivoAprendizajeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarObjetivo(objetivo: ObjetivoAprendizajeEntity)

    @Update
    suspend fun actualizarObjetivo(objetivo: ObjetivoAprendizajeEntity)

    @Query("UPDATE objetivos_aprendizaje SET estado = :estado WHERE id = :id")
    suspend fun actualizarEstadoObjetivo(id: String, estado: EstadoObjetivo)

    @Query("UPDATE objetivos_aprendizaje SET estaAsignadoAlCurso = :estaAsignado, intentosMaximos = :intentosMaximos, fechaInicioDisponibilidad = :fechaInicio, fechaFinDisponibilidad = :fechaFin WHERE id = :id")
    suspend fun actualizarAsignacion(id: String, estaAsignado: Boolean, intentosMaximos: Int, fechaInicio: String, fechaFin: String)
}
