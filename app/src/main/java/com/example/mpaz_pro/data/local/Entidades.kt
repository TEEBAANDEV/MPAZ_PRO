package com.example.mpaz_pro.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey val nombreUsuario: String,
    val clave: String,
    val nombreCompleto: String,
    val rol: String
)

@Entity(tableName = "objetivos")
data class ObjetivoEntity(
    @PrimaryKey val id: String,
    val idAsignatura: String,
    val nombreAsignatura: String,
    val codigo: String,
    val titulo: String,
    val descripcion: String,
    val pregunta: String,
    val opciones: String,
    val indiceCorrecto: Int,
    val estado: String = "AUTORIZADO",
    val asignadoAlCurso: Boolean = true,
    val intentosMaximos: Int = 2
)

@Entity(tableName = "informes")
data class InformeEntity(
    @PrimaryKey val id: String,
    val idEstudiante: String,
    val nombreEstudiante: String,
    val curso: String = "2° Básico A",
    val asignatura: String,
    val porcentajeLogro: Int,
    val desafiosCompletados: Int,
    val observacionDocente: String,
    val fecha: String
)
