package com.example.mpaz_pro.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

    @Query("SELECT * FROM usuarios WHERE nombreUsuario = :usuario AND clave = :clave LIMIT 1")
    suspend fun iniciarSesion(usuario: String, clave: String): UsuarioEntity?

    @Query("SELECT * FROM usuarios WHERE nombreUsuario = :nombreUsuario LIMIT 1")
    fun obtenerUsuarioPorNombre(nombreUsuario: String): Flow<UsuarioEntity?>

    @Query("UPDATE usuarios SET fotoPerfilUri = :uri WHERE nombreUsuario = :nombreUsuario")
    suspend fun actualizarFotoPerfil(nombreUsuario: String, uri: String?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarUsuarios(usuarios: List<UsuarioEntity>)

    @Query("SELECT * FROM objetivos")
    fun obtenerTodosLosObjetivos(): Flow<List<ObjetivoEntity>>

    @Query("SELECT * FROM objetivos WHERE asignadoAlCurso = 1")
    fun obtenerObjetivosAsignados(): Flow<List<ObjetivoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarObjetivo(objetivo: ObjetivoEntity)

    @Query("UPDATE objetivos SET estado = :nuevoEstado WHERE id = :id")
    suspend fun actualizarEstadoObjetivo(id: String, nuevoEstado: String)

    @Query("UPDATE objetivos SET asignadoAlCurso = :asignado WHERE id = :id")
    suspend fun actualizarAsignacionObjetivo(id: String, asignado: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarObjetivos(objetivos: List<ObjetivoEntity>)

    @Query("SELECT * FROM informes")
    fun obtenerTodosLosInformes(): Flow<List<InformeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarInforme(informe: InformeEntity)

    @Query("UPDATE informes SET observacionDocente = :observacion WHERE id = :id")
    suspend fun actualizarObservacionInforme(id: String, observacion: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarInformes(informes: List<InformeEntity>)

    @Query("SELECT * FROM notas ORDER BY id DESC")
    fun obtenerTodasLasNotas(): Flow<List<NotaEntity>>

    @Query("SELECT * FROM notas WHERE idEstudiante = :idEstudiante ORDER BY id DESC")
    fun obtenerNotasPorEstudiante(idEstudiante: String): Flow<List<NotaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarNota(nota: NotaEntity)

    @Query("DELETE FROM notas WHERE id = :id")
    suspend fun eliminarNota(id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarNotas(notas: List<NotaEntity>)
}
