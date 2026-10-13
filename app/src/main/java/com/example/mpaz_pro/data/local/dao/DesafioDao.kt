package com.example.mpaz_pro.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mpaz_pro.data.local.entity.DesafioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DesafioDao {
    @Query("SELECT * FROM desafios WHERE idObjetivo = :idObjetivo ORDER BY ordenSecuencia ASC")
    fun obtenerDesafiosPorObjetivo(idObjetivo: String): Flow<List<DesafioEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarDesafios(desafios: List<DesafioEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarDesafio(desafio: DesafioEntity)
}
