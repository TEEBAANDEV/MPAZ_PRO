package com.example.mpaz_pro.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mpaz_pro.data.local.entity.ContenidoDidacticoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContenidoDidacticoDao {
    @Query("SELECT * FROM contenidos_didacticos WHERE idObjetivo = :idObjetivo ORDER BY ordenSecuencia ASC")
    fun obtenerContenidosPorObjetivo(idObjetivo: String): Flow<List<ContenidoDidacticoEntity>>

    @Query("SELECT * FROM contenidos_didacticos WHERE idObjetivo = :idObjetivo ORDER BY ordenSecuencia ASC LIMIT 1")
    suspend fun obtenerPrimerContenidoPorObjetivo(idObjetivo: String): ContenidoDidacticoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarContenidos(contenidos: List<ContenidoDidacticoEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarContenido(contenido: ContenidoDidacticoEntity)
}
