package com.example.mpaz_pro.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mpaz_pro.data.local.entity.AsignaturaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AsignaturaDao {
    @Query("SELECT * FROM asignaturas")
    fun obtenerTodasLasAsignaturas(): Flow<List<AsignaturaEntity>>

    @Query("SELECT * FROM asignaturas WHERE id = :id LIMIT 1")
    suspend fun obtenerAsignaturaPorId(id: String): AsignaturaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarAsignaturas(asignaturas: List<AsignaturaEntity>)
}
