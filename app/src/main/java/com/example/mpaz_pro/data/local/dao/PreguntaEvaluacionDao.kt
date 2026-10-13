package com.example.mpaz_pro.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mpaz_pro.data.local.entity.PreguntaEvaluacionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PreguntaEvaluacionDao {
    @Query("SELECT * FROM preguntas_evaluacion WHERE idObjetivo = :idObjetivo ORDER BY numeroPregunta ASC")
    fun obtenerPreguntasPorObjetivo(idObjetivo: String): Flow<List<PreguntaEvaluacionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarPreguntasEvaluacion(preguntas: List<PreguntaEvaluacionEntity>)
}
