package com.example.mpaz_pro.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mpaz_pro.data.local.entity.InformePedagogicoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InformePedagogicoDao {
    @Query("SELECT * FROM informes_pedagogicos")
    fun obtenerTodosLosInformes(): Flow<List<InformePedagogicoEntity>>

    @Query("SELECT * FROM informes_pedagogicos WHERE id = :idInforme LIMIT 1")
    suspend fun obtenerInformePorId(idInforme: String): InformePedagogicoEntity?

    @Query("SELECT * FROM informes_pedagogicos WHERE idEstudiante = :idEstudiante")
    fun obtenerInformesPorEstudiante(idEstudiante: String): Flow<List<InformePedagogicoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarInforme(informe: InformePedagogicoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarInformes(informes: List<InformePedagogicoEntity>)

    @Update
    suspend fun actualizarInforme(informe: InformePedagogicoEntity)

    @Query("UPDATE informes_pedagogicos SET observacionDocente = :observacion, estaValidadoPorDocente = 1 WHERE id = :idInforme")
    suspend fun actualizarObservacionDocente(idInforme: String, observacion: String)
}
