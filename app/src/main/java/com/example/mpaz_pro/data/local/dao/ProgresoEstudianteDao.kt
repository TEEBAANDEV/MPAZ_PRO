package com.example.mpaz_pro.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mpaz_pro.data.local.entity.ProgresoEstudianteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgresoEstudianteDao {
    @Query("SELECT * FROM progreso_estudiante WHERE idEstudiante = :idEstudiante")
    fun obtenerProgresoPorEstudiante(idEstudiante: String): Flow<List<ProgresoEstudianteEntity>>

    @Query("SELECT * FROM progreso_estudiante WHERE idEstudiante = :idEstudiante AND idObjetivo = :idObjetivo LIMIT 1")
    suspend fun obtenerProgresoPorObjetivo(idEstudiante: String, idObjetivo: String): ProgresoEstudianteEntity?

    @Query("SELECT * FROM progreso_estudiante WHERE idObjetivo = :idObjetivo")
    fun obtenerTodosLosProgresosPorObjetivo(idObjetivo: String): Flow<List<ProgresoEstudianteEntity>>

    @Query("SELECT * FROM progreso_estudiante")
    fun obtenerTodosLosProgresos(): Flow<List<ProgresoEstudianteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarProgreso(progreso: ProgresoEstudianteEntity)

    @Update
    suspend fun actualizarProgreso(progreso: ProgresoEstudianteEntity)
}
