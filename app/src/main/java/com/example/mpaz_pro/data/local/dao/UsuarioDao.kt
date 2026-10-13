package com.example.mpaz_pro.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mpaz_pro.data.local.entity.RolUsuario
import com.example.mpaz_pro.data.local.entity.UsuarioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuarios WHERE nombreUsuario = :nombreUsuario AND claveHash = :clave LIMIT 1")
    suspend fun iniciarSesion(nombreUsuario: String, clave: String): UsuarioEntity?

    @Query("SELECT * FROM usuarios WHERE id = :idUsuario LIMIT 1")
    suspend fun obtenerUsuarioPorId(idUsuario: String): UsuarioEntity?

    @Query("SELECT * FROM usuarios WHERE rol = :rol")
    fun obtenerUsuariosPorRol(rol: RolUsuario): Flow<List<UsuarioEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarUsuarios(usuarios: List<UsuarioEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarUsuario(usuario: UsuarioEntity)
}
